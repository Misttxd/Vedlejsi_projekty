ALTER TABLE z_Article 
ADD (
    article_author_count INT, 
    article_institution_count INT
    )

UPDATE z_Article 
SET article_author_count = author_count, 
    article_institution_count = institution_count;


CREATE OR REPLACE FUNCTION StatisticsConsistent (p_processType VARCHAR2) RETURN VARCHAR2 AS
    v_pocetSpatnych NUMBER := 0;
BEGIN
    -- 1. Kontrola vstupu: Pokud zadá nesmysl, okamžitě vyhodíme chybu
    IF p_processType NOT IN ('Analyze', 'Fix') THEN
        RAISE_APPLICATION_ERROR(-20001, 'Neplatny parametr. Zadejte Analyze nebo Fix.');
    END IF;

    -- 2. Spočítáme, kolik článků má špatná data
    -- Porovnáváme uložené číslo se SKUTEČNÝM počtem řádků pomocí poddotazu (subquery)
    SELECT COUNT(*) INTO v_pocetSpatnych 
    FROM z_article a
    WHERE a.article_author_count != (SELECT COUNT(*) FROM z_article_author aa WHERE aa.aid = a.aid)
       OR a.article_institution_count != (SELECT COUNT(*) FROM z_article_institution ai WHERE ai.aid = a.aid);

    -- 3. Větev 'Analyze': Jen vracíme výsledek
    IF p_processType = 'Analyze' THEN
        IF v_pocetSpatnych > 0 THEN
            RETURN 'TRUE';
        ELSE
            RETURN 'FALSE';
        END IF;
    END IF;

    -- 4. Větev 'Fix': Opravíme to elegantním UPDATEm
    IF p_processType = 'Fix' THEN
        -- Přepíšeme hodnoty skutečnými počty ze spojovacích tabulek, ale jen u těch, kde to nesedí
        UPDATE z_article a
        SET article_author_count = (SELECT COUNT(*) FROM z_article_author aa WHERE aa.aid = a.aid),
            article_institution_count = (SELECT COUNT(*) FROM z_article_institution ai WHERE ai.aid = a.aid)
        WHERE a.article_author_count != (SELECT COUNT(*) FROM z_article_author aa WHERE aa.aid = a.aid)
           OR a.article_institution_count != (SELECT COUNT(*) FROM z_article_institution ai WHERE ai.aid = a.aid);
        
        RETURN 'TRUE'; -- Pokud UPDATE projde, vracíme TRUE
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RETURN 'FALSE'; -- Pokud to někde spadne (třeba při Fixu), vracíme FALSE
END;


--2
CREATE OR REPLACE PROCEDURE ReportFord4Institution (p_institution_name z_institution.name%TYPE) AS
    v_count NUMBER;

    i_name z_institution.NAME%TYPE;
    i_iid z_institution.iid%TYPE;

    v_counter NUMBER := 1;
BEGIN
    select count(*) into v_count
    from Z_INSTITUTION i
    where i.name = p_institution_name;


    IF v_count = 0 THEN
        dbms_output.put_line('Instituce neexistuje');
        return;
    ELSIF v_count > 1 THEN
        dbms_output.put_line('Nejednoznačný název instituce');
        return;
    END IF;

    select i.name, i.iid into i_name, i_iid --dochází mi že si znova ukládat jméno instituce je nepotřebné, ale hlavně mě zajímá jestli takhle funguje ukládání více hodnot ze selectu.
    from Z_INSTITUTION i
    where i.name = p_institution_name;

    dbms_output.put_line('Název instituce: ' || p_institution_name|| ', iid instituce: ' || i_iid ||':');

    for vysledek in (
        select distinct ff.NAME
        from Z_INSTITUTION i
        join Z_ARTICLE_INSTITUTION ai on ai.iid = i.iid
        join Z_ARTICLE a on a.aid = ai.AID
        join z_journal j on j.jid = a.JID
        join Z_YEAR_FIELD_JOURNAL yfj on yfj.jid = j.jid and yfj.year = a.year
        join Z_FIELD_FORD ff on ff.fid = yfj.FID
        where i.name = i_name
        ) LOOP
            dbms_output.PUT_LINE(''|| v_counter ||'. ' || vysledek.name );
            v_counter := v_counter +1;

    END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('neco je spatne');

END;

select i.name
from Z_INSTITUTION i
where i.name = 'Sanofi s.r.o.';

EXECUTE ReportFord4Institution ('Agrotest fyto, s.r.o.');