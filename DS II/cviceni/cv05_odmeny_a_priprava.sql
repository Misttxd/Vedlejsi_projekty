SET SERVEROUTPUT ON;

/*
	CV05 - Odměny a příprava na test
	Témata:
	1) příprava pomocné tabulky s odměnami
	2) výpočet odměny podle rankingu časopisu
	3) bonusy/penalizace podle institucí
	4) hromadný přepočet odměn a jednoduchý report
*/

-- 1.1
CREATE OR REPLACE PROCEDURE PPrepareTableReward AS
BEGIN
	BEGIN
		EXECUTE IMMEDIATE 'DROP TABLE z_article_reward';
	EXCEPTION
		WHEN OTHERS THEN
			NULL;
	END;

	EXECUTE IMMEDIATE '
		CREATE TABLE z_article_reward (
			aid NUMBER PRIMARY KEY,
			reward_amount NUMBER(12,0) NOT NULL,
			reward_note VARCHAR2(200),
			last_update DATE DEFAULT SYSDATE
		)
	';
END;


-- 1.2
CREATE OR REPLACE FUNCTION FRankingBaseReward(
	p_ranking VARCHAR2
) RETURN NUMBER AS
BEGIN
	RETURN CASE UPPER(TRIM(p_ranking))
		WHEN 'DECIL' THEN 100000
		WHEN 'Q1' THEN 50000
		WHEN 'Q2' THEN 25000
		WHEN 'Q3' THEN 15000
		WHEN 'Q4' THEN 10000
		ELSE 0
	END;
END;


-- 1.3
CREATE OR REPLACE FUNCTION FArticleInstitutionBonus(
	p_aid NUMBER
) RETURN NUMBER AS
	v_bonus NUMBER := 0;
	v_ostrava_cnt NUMBER := 0;
	v_brno_cnt NUMBER := 0;
BEGIN
	SELECT COUNT(*) INTO v_ostrava_cnt
	FROM z_article_institution ai
	JOIN z_institution i ON i.iid = ai.iid
	WHERE ai.aid = p_aid
	  AND UPPER(i.town) LIKE 'OSTRAVA%';

	SELECT COUNT(*) INTO v_brno_cnt
	FROM z_article_institution ai
	JOIN z_institution i ON i.iid = ai.iid
	WHERE ai.aid = p_aid
	  AND UPPER(i.town) LIKE 'BRNO%';

	IF v_ostrava_cnt > 0 THEN
		v_bonus := v_bonus + 10000;
	END IF;

	IF v_brno_cnt > 0 THEN
		v_bonus := v_bonus - 10000;
	END IF;

	RETURN v_bonus;
END;


-- 1.4
CREATE OR REPLACE FUNCTION FGetArticleReward(
	p_aid NUMBER
) RETURN NUMBER AS
	v_base_reward NUMBER := 0;
	v_bonus NUMBER := 0;
BEGIN
	SELECT NVL(MAX(FRankingBaseReward(yfj.ranking)), 0)
	INTO v_base_reward
	FROM z_article a
	JOIN z_year_field_journal yfj
	  ON yfj.jid = a.jid
	 AND yfj.year = a.year
	WHERE a.aid = p_aid;

	v_bonus := FArticleInstitutionBonus(p_aid);

	RETURN GREATEST(v_base_reward + v_bonus, 0);
EXCEPTION
	WHEN NO_DATA_FOUND THEN
		RETURN 0;
END;


-- 1.5
CREATE OR REPLACE PROCEDURE PRebuildArticleRewards(
	p_year NUMBER DEFAULT NULL
) AS
BEGIN
	DELETE FROM z_article_reward;

	FOR r_article IN (
		SELECT aid, year
		FROM z_article
		WHERE p_year IS NULL OR year = p_year
	) LOOP
		INSERT INTO z_article_reward (aid, reward_amount, reward_note, last_update)
		VALUES (
			r_article.aid,
			FGetArticleReward(r_article.aid),
			CASE
				WHEN p_year IS NULL THEN 'Úplný přepočet'
				ELSE 'Filtr podle roku ' || p_year
			END,
			SYSDATE
		);
	END LOOP;

	COMMIT;
END;


-- 1.6
CREATE OR REPLACE PROCEDURE PPrintTopRewards(
	p_limit NUMBER DEFAULT 10
) AS
	v_rank NUMBER := 0;
BEGIN
	FOR r_item IN (
		SELECT a.aid, a.name, rr.reward_amount
		FROM z_article_reward rr
		JOIN z_article a ON a.aid = rr.aid
		ORDER BY rr.reward_amount DESC, a.aid
	) LOOP
		EXIT WHEN v_rank >= p_limit;

		v_rank := v_rank + 1;
		DBMS_OUTPUT.PUT_LINE(
			LPAD(v_rank, 2, '0') || '. ' ||
			'AID=' || r_item.aid || ' | ' ||
			'Reward=' || r_item.reward_amount || ' | ' ||
			'Name=' || r_item.name
		);
	END LOOP;
END;

