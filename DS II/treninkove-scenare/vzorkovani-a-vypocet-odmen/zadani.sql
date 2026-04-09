-- 1

CREATE OR REPLACE PROCEDURE PSampleIntColumn (p_table VARCHAR2, p_column VARCHAR2, p_sample_rate INT) AS 
    v_sample_table VARCHAR2(100);

    v_count NUMBER;

    my_exception EXCEPTION;
BEGIN

    IF p_sample_rate NOT BETWEEN 0 AND 100 THEN
        
        raise my_exception;

    END IF;

    v_sample_table := p_table||'_sample';

    select count(*) into v_count
    from user_tables
    where table_name = UPPER(v_sample_table);

    IF v_count >= 1 THEN
        EXECUTE IMMEDIATE 'DROP TABLE '|| v_sample_table;
    END IF;

    EXECUTE IMMEDIATE'
        CREATE TABLE '|| v_sample_table ||'(
        '|| p_column ||' INT
        )';

    EXECUTE IMMEDIATE'
        INSERT INTO ' ||v_sample_table||' ('||p_column||')
        SELECT '|| p_column ||'
        FROM '|| p_table ||' SAMPLE('||p_sample_rate||')
    ';
    

EXCEPTION
    WHEN my_exception THEN
        dbms_output.put_line('neco je spatne');

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE;


END;

CREATE OR REPLACE FUNCTION FGetArticleReward (p_aid z_Article.aid%TYPE) RETURN INT AS

    v_best_rating VARCHAR2(10);

    v_institution VARCHAR2(100);

    v_count_ostrava NUMBER;
    v_count_brno NUMBER;

    v_castka INT := 0;
BEGIN

    dbms_output.put_line('p_aid');

    select min(yfj.RANKING) into v_best_rating
    from Z_ARTICLE a, Z_JOURNAL j, Z_YEAR_FIELD_JOURNAL yfj
    where a.jid = j.jid and j.jid =yfj.jid and a.year = yfj.year and a.aid = p_aid;

    IF v_best_rating = 'Decil' THEN
        v_castka := v_castka + 100000;
    ELSIF v_best_rating = 'Q1' THEN
        v_castka := v_castka + 50000;
    ELSIF v_best_rating = 'Q2' THEN
        v_castka := v_castka + 25000;
    ELSIF v_best_rating = 'Q3' THEN
        v_castka := v_castka + 15000;
    ELSIF v_best_rating = 'Q4' THEN
        v_castka := v_castka + 5000;
    END IF;


    select count(*) into v_count_ostrava
    from Z_ARTICLE a
    join z_article_institution ai on a.aid = ai.aid
    join z_institution i on i.iid = ai.iid
    where a.aid = p_aid and i.TOWN LIKE '%OSTRAVA%';

    IF v_count_ostrava > 0 THEN
        v_castka := v_castka + 10000;
    END IF;

    select count(*) into v_count_brno
    from Z_ARTICLE a
    join z_article_institution ai on a.aid = ai.aid
    join z_institution i on i.iid = ai.iid
    where a.aid = p_aid and i.TOWN LIKE '%BRNO%';
    IF v_count_brno > 0 THEN
        v_castka := v_castka - 10000;
    END IF;


    if v_castka < 0 THEN
        dbms_output.put_line('nevim co se ma presne stat, hadam, ze return null ');
        v_castka := 0;
    end if;

    return v_castka;

END;

