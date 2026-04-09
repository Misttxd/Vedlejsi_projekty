SET SERVEROUTPUT ON;


--1
CREATE OR REPLACE PROCEDURE P_CreateTestTables AS

v_nazev VARCHAR2(50);
v_table_count NUMBER;

BEGIN

    FOR selected IN (select sid from z_field_of_science) LOOP

        IF selected is NULL THEN
            RETURN;
        END IF;


        v_nazev := 'test_' || selected.sid;
        dbms_output.PUT_LINE(v_nazev);
        --tady se dale hadam vytvori pro kazdy nazev tabulka, takze to bude ve finale vypadat nejak takto:


        select count(*) into v_table_count
        from USER_TABLES
        where table_name = UPPER(v_nazev); --V TABULCE JE TO VZDY ULOZENO VELKYMI PISMENY

        IF v_table_count > 0 THEN 
            EXECUTE IMMEDIATE ('
                DROP TABLE ' || v_nazev || '
            ');
        END IF;

        EXECUTE IMMEDIATE '
            CREATE TABLE ' || v_nazev || ' (
                fid number,
                NAME VARCHAR2(100),
                YEAR NUMBER,
                RANKING VARCHAR2(10),
                TOTAL_COUNT NUMBER
            )';


        EXECUTE IMMEDIATE ('
            INSERT INTO ' || v_nazev || '(fid, name, year, ranking, total_count)
            SELECT ff.fid, ff.name, yfj.year, yfj.ranking, COUNT(yfj.jid)
            from z_field_ford ff
            join z_year_field_journal yfj on yfj.fid = ff.fid
            where ff.sid = :1
            group by ff.fid, ff.name, yfj.year, yfj.ranking 
        ') USING selected.sid;
    END LOOP;
END;


--2
DROP TABLE z_AA2;
CREATE TABLE z_AA2 as SELECT * from Z_ARTICLE_AUTHOR;

DROP TABLE z_A2;
CREATE TABLE z_A2 as select * from Z_ARTICLE;

CREATE OR REPLACE TRIGGER T_AuthorWatchdog AFTER DELETE OR INSERT OR UPDATE ON z_AA2 FOR EACH ROW

BEGIN
    IF INSERTING THEN
        UPDATE Z_A2
        SET AUTHOR_COUNT = AUTHOR_COUNT + 1
        where aid = :new.aid;

    ELSIF DELETING THEN
        UPDATE Z_A2
        SET AUTHOR_COUNT = AUTHOR_COUNT - 1
        where aid = :old.aid;

    ELSIF UPDATING THEN
        IF :OLD.aid != :NEW.aid THEN
            -- Odebereme z původního článku
            UPDATE z_A2 
            SET author_count = NVL(author_count, 0) - 1 
            WHERE aid = :OLD.aid;

            -- Přidáme k novému článku
            UPDATE z_A2 
            SET author_count = NVL(author_count, 0) + 1 
            WHERE aid = :NEW.aid;
        END IF;
    END IF;

END;

SELECT aid, author_count 
FROM z_A2 
WHERE aid = 1;

INSERT INTO z_AA2 (aid, rid) 
VALUES (1, 100);

DELETE FROM z_AA2 
WHERE aid = 1 AND rid = 100;