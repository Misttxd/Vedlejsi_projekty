
/*****************************************
************* VARIANTA A *****************
*****************************************/

/**
  Cílem úkolu je vytvořit proceduru `PSampleIntColumn`, která bude provádět následující kroky:

1. Přijme tři parametry:
   - `p_table` (název tabulky)
   - `p_column` (název sloupce)
   - `p_sample_rate` (míra vzorkování v procentech)

2. Zkontroluje, zda je hodnota `p_sample_rate` v rozmezí 0 až 100. Pokud není, vyvolá výjimku.
3. Vytvoří novou tabulku s názvem `<p_table>_SAMPLE`, která bude obsahovat pouze sloupec `<p_column>` typu INT. Pokud tabulka již existuje, bude smazána a znovu vytvořena.
4. Na základě `p_sample_rate` navzorkuje sloupec `p_column` z tabulky `p_table` do vytvořené tabulky `<p_table>_SAMPLE.
5. Pokud dojde k chybě, vypíše chybovou zprávu a vyvolá výjimku.

Pro otestování procedury spusťte s v platnými i neplatnými parametry.
 */

 -- rozdeleni na varianty: jedna procedura je pro INT a druha pro VARCHAR2, jedna prijma sample rate jako procenta a druha jako pocet zaznamu
create or replace procedure PSampleIntColumn(p_table VARCHAR2, p_column VARCHAR2, p_sample_rate INT)
AS
    TYPE rc IS REF CURSOR;
    v_sql               VARCHAR2(2048);
    v_sample_every      INT DEFAULT 0;
    c_cur               rc;
    v_int_col           INT;
    v_counter           INT DEFAULT 0;
    v_sample_table_name VARCHAR2(128);
    v_invalid_sample_rate EXCEPTION;
BEGIN
    IF p_sample_rate < 0 OR p_sample_rate > 100 THEN
        RAISE v_invalid_sample_rate;
    END IF;

    v_sample_table_name := p_table || '_SAMPLE';
    BEGIN
        EXECUTE IMMEDIATE ('DROP TABLE ' || v_sample_table_name);
    EXCEPTION
        WHEN OTHERS THEN
            NULL;
    END;

    v_sql := 'CREATE TABLE ' || v_sample_table_name || '(' || p_column || ' INT)';

    EXECUTE IMMEDIATE v_sql;
    v_sample_every := CEIL(100 / p_sample_rate);
    v_sql := 'SELECT ' || p_column || ' FROM ' || UPPER(p_table);
    OPEN c_cur FOR v_sql;

    LOOP
        FETCH c_cur INTO v_int_col;
        EXIT WHEN c_cur%NOTFOUND;

        IF v_counter >= v_sample_every THEN
            EXECUTE IMMEDIATE 'INSERT INTO ' || v_sample_table_name || ' VALUES (:1)' USING v_int_col;
            v_counter := 0;
        end if;
        v_counter := v_counter + 1;
    END LOOP;
    CLOSE c_cur;
EXCEPTION
    WHEN v_invalid_sample_rate THEN
        DBMS_OUTPUT.PUT_LINE('Sample rate must be between 0 and 100');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE;

END;




/**
  Napiste funkci, ktera bere jako parametr ID clanku a vrati "vydelane" penize za clanek. Odmena za clanek
  je urcena podle rankingu casopisu, ve kterem byl clanek publikovan. Odmena je dana nasledovne:
    - ranking "Decil": 100 000
    - ranking "Q1": 50 000
    - ranking "Q2": 25 000
    - ranking "Q3": 15 000
    - ranking "Q4": 10 000
  Pokud clanek vysel ve vice casopisech, odmena je soucet odmen za kazdy casopis.
  Pokud se na clanku podilela instituce z Ostravy, odmena se zvysi o 10 000.
  Pokud se na clanku podilela instituce z Brna, odmena se snizi o 10 000.
 */
create or replace function FGetArticleReward(p_article_id INT) RETURN INT
AS
    v_reward      INT;
    v_ostrava_authors INT;
    v_brno_authors INT;
BEGIN
    with cte as (select          aid,
                                 ranking,
                                 CASE (RANKING)
                                     WHEN 'Decil' THEN 100000
                                     WHEN 'Q1' THEN 50000
                                     WHEN 'Q2' THEN 25000
                                     WHEN 'Q3' THEN 15000
                                     WHEN 'Q4' THEN 10000 END as reward
                 from Z_YEAR_FIELD_JOURNAL yfj
                          join Z_ARTICLE a on yfj.JID = a.JID AND yfj.YEAR = a.YEAR
                 where AID = p_article_id)
    select max(reward)
    INTO v_reward
    from cte;

    SELECT COALESCE(COUNT(*), 0) INTO v_ostrava_authors
    FROM Z_ARTICLE_INSTITUTION ai
    JOIN Z_INSTITUTION i ON ai.IID = i.IID
    where ai.AID = p_article_id AND i.town LIKE 'Ostrava%';

    SELECT COALESCE(COUNT(*), 0) INTO v_brno_authors
    FROM Z_ARTICLE_INSTITUTION ai
    JOIN Z_INSTITUTION i ON ai.IID = i.IID
    where ai.AID = p_article_id AND i.town LIKE 'Brno%';

    IF COALESCE(v_ostrava_authors, 0) > 0 THEN
        v_reward := v_reward + 10000;
    END IF;

    IF COALESCE(v_brno_authors, 0) > 0 THEN
        v_reward := v_reward - 10000;
    END IF;

    RETURN GREATEST(v_reward, 0);
END;



/*****************************************
************* VARIANTA B *****************
*****************************************/

create or replace procedure PSampleVarcharColumn(p_table VARCHAR2, p_column VARCHAR2, p_sample_count INT)
AS
    TYPE rc IS REF CURSOR;
    v_sql               VARCHAR2(2048);
    v_sample_every      INT DEFAULT 0;
    c_cur               rc;
    v_char_col          VARCHAR2(1024);
    v_counter           INT DEFAULT 0;
    v_sample_table_name VARCHAR2(128);
    v_invalid_sample_count EXCEPTION;
    v_total_count INT;
BEGIN
    execute immediate 'select count(*) from ' || p_table into v_total_count;
    IF p_sample_count < 0 OR p_sample_count > v_total_count THEN
        RAISE v_invalid_sample_count;
    END IF;

    v_sample_table_name := p_table || '_SAMPLE';
    BEGIN
        EXECUTE IMMEDIATE ('DROP TABLE ' || v_sample_table_name);
    EXCEPTION
        WHEN OTHERS THEN
            NULL;
    END;

    v_sql := 'CREATE TABLE ' || v_sample_table_name || '(' || p_column || ' VARCHAR(1024))';

    EXECUTE IMMEDIATE v_sql;
    v_sample_every := FLOOR(v_total_count / p_sample_count);
    v_sql := 'SELECT ' || p_column || ' FROM ' || UPPER(p_table);
    OPEN c_cur FOR v_sql;

    LOOP
        FETCH c_cur INTO v_char_col;

        EXIT WHEN c_cur%NOTFOUND;

        IF v_counter >= v_sample_every THEN
            EXECUTE IMMEDIATE 'INSERT INTO ' || v_sample_table_name || ' VALUES (:2)' USING v_char_col;
            v_counter := 0;
        end if;
        v_counter := v_counter + 1;
    END LOOP;
    CLOSE c_cur;
EXCEPTION
    WHEN v_invalid_sample_count THEN
        DBMS_OUTPUT.PUT_LINE('Sample rate must be between 0 and 100');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE;
END;

create or replace function FGetArticleReward(p_article_id INT) RETURN INT
AS
    v_reward      INT;
    v_praha_authors INT;
    v_olomouc_authors INT;
BEGIN
    with cte as (select          aid,
                                 ranking,
                                 CASE (RANKING)
                                     WHEN 'Decil' THEN 75000
                                     WHEN 'Q1' THEN 35000
                                     WHEN 'Q2' THEN 20000
                                     WHEN 'Q3' THEN 10000
                                     WHEN 'Q4' THEN 7500 END as reward
                 from Z_YEAR_FIELD_JOURNAL yfj
                          join Z_ARTICLE a on yfj.JID = a.JID AND yfj.YEAR = a.YEAR
                 where AID = p_article_id)
    select max(reward)
    INTO v_reward
    from cte;

    SELECT COALESCE(COUNT(*), 0) INTO v_praha_authors
    FROM Z_ARTICLE_INSTITUTION ai
    JOIN Z_INSTITUTION i ON ai.IID = i.IID
    where ai.AID = p_article_id AND i.town LIKE 'Praha%';

    SELECT COALESCE(COUNT(*), 0) INTO v_olomouc_authors
    FROM Z_ARTICLE_INSTITUTION ai
    JOIN Z_INSTITUTION i ON ai.IID = i.IID
    where ai.AID = p_article_id AND i.town LIKE 'Olomouc%';

    IF COALESCE(v_praha_authors, 0) > 0 THEN
        v_reward := v_reward + 10000;
    END IF;

    IF COALESCE(v_olomouc_authors, 0) > 0 THEN
        v_reward := v_reward - 10000;
    END IF;

    RETURN GREATEST(v_reward, 0);
END;

