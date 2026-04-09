/*
  1)
  Napište proceduru, která vytvoří nové tabulky podle rozdělení z_field_of_science (6 tabulek) s názvy test_SID, příklad: test_1
  V každé z tabulek budou obsaženy informace rozdělení článků podle ford z tabulky z_field_ford a z_year_field_journal
  , tak že ke každému FID a NAME z ford doplníte YEAR (příklad 2022), RANKING (příklad Q1) a celkový počet časopísů (TOTAL_COUNT) v daném roce s daným rankingem 
  pro všechny dostupní rankingy a roky. Struktura výsledné tabulky test_ (FID, NAME, YEAR, RANKING, TOTAL_COUNT)
  Pokud by při generování tabulka s daným názvem již existovala (její název existuje v tabulce user_tables), pak ji před vytvořením nejprve odstraníte.
  Připravte druhou verzi procedury, která namísto vymazní tabulky vyvolá vlastní výjimku s hláškou 'Tabulka: test_1 již existuje.'.
  (Příklad: select count(*) into c from user_tables where table_name = upper('test_1')).
*/
  
SET SERVEROUTPUT ON;
  
CREATE OR REPLACE PROCEDURE GenerateReport
AS
  v_sid NUMBER;
  v_table_name VARCHAR(20);
  v_count INTEGER;
  
  CURSOR c_field_of_science
  IS
    SELECT SID
    FROM z_field_of_science
    ORDER BY SID ASC;
  moje_vyjimka EXCEPTION;

BEGIN
  OPEN c_field_of_science;
  LOOP
    FETCH c_field_of_science INTO v_sid;
    EXIT WHEN c_field_of_science%NOTFOUND;

    v_table_name := 'test_' || to_char(v_sid);

    SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = UPPER(v_table_name);

    IF v_count = 1 THEN
      EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name; 
      --  RAISE moje_vyjimka;    -- pro variantu B se vymění drop za RAISE        
    END IF;

    EXECUTE IMMEDIATE 'CREATE TABLE ' || v_table_name || ' AS
      SELECT f.FID, f.NAME, y.YEAR, y.RANKING, SUM(y.ARTICLE_COUNT) AS TOTAL_COUNT FROM
      z_field_ford f JOIN z_year_field_journal y ON (f.FID = y.FID)
      WHERE SID = ' || to_char(v_sid) || '  
      GROUP BY f.FID, f.NAME, y.YEAR, y.RANKING';

    dbms_output.put_line(v_table_name);
        
  END LOOP;
  CLOSE c_field_of_science;

EXCEPTION
  WHEN moje_vyjimka THEN
    dbms_output.put_line('Tabulka: ' || v_table_name || ' jiz existuje.');
END;
  
EXECUTE GenerateReport;
  
-- col name for a40; 
-- select * from test_1 order by name, year
-- select * from test_2 order by name, year


  
  /*
  2)
  Napište trigger nebo sadu triggerů, který při přidání, odstranění nebo změně autora (z_article_author) nebo instituce (z_article_institution) k článku přepočte
  počet všech autorů AUTHOR_COUNT a INSTITUTION_COUNT daného článku (z_article). Uvědomte si, u kterých tabulek a pro které DML operace musí být trigger definován.
  
  
  Nástin řešení:
  Trigger by měl být definován jako AFTER INSERT UPDATE a DELETE pro tabulky (z_article_author) a (z_article_institution)
  a bude updatovat hodnotu atributu AUTHOR_COUNT a INSTITUTION_COUNT v tabulce (z_article)
  */
  


  CREATE OR REPLACE PROCEDURE CreateWorkTable(p_table_name VARCHAR2)
  AS
    v_count INTEGER;
  BEGIN
  
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'Z_WORK';
  
    IF v_count = 1 THEN
      EXECUTE IMMEDIATE 'DROP TABLE z_work'; 
      --  RAISE moje_vyjimka;    -- pro variantu B se vymění drop za RAISE        
    END IF;
  
  EXECUTE IMMEDIATE 'CREATE TABLE Z_WORK AS SELECT * FROM ' || p_table_name;
  
  END;
  
  execute CreateWorkTable('z_article_author');



/*

	Nefunkční řešení nahrazeno novým řešením s třemi triggery.

  CREATE OR REPLACE TRIGGER recalc_author_count AFTER
  INSERT OR UPDATE OR DELETE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN
  
    SELECT COUNT(*) INTO v_count FROM z_work WHERE aid=:new.aid;

    UPDATE z_article SET author_count = v_count WHERE aid=:new.aid;
  
  END;
  


  CREATE OR REPLACE TRIGGER recalc_institution_count AFTER
  INSERT OR UPDATE OR DELETE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN
  
    SELECT COUNT(*) INTO v_count FROM z_work WHERE aid=:new.aid;

    UPDATE z_article SET institution_count = v_count WHERE aid=:new.aid;
  
  END;

*/

-- Nove reseni pocita s korektni hodnotou atributu article_count a institution_count,
-- v databazi rvvi2023 tomu tak ale neni.

 /* Řešení pro Autory */

    CREATE OR REPLACE TRIGGER recalc_author_insert_count AFTER INSERT ON z_work FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article 
      SET author_count = author_count + 1 
      WHERE aid=:new.aid;
  
  END;
  
    CREATE OR REPLACE TRIGGER recalc_author_update_count AFTER
  UPDATE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article SET author_count = author_count - 1 WHERE aid=:old.aid;
      UPDATE z_article SET author_count = author_count + 1 WHERE aid=:new.aid;
  
  END;
  
    
    CREATE OR REPLACE TRIGGER recalc_author_delete_count AFTER
  DELETE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article SET author_count = author_count - 1 WHERE aid=:old.aid;
  
  END;

 /* Řešení pro Instituce */


    CREATE OR REPLACE TRIGGER recalc_institution_insert_count AFTER
  INSERT ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article SET institution_count = institution_count + 1 WHERE aid=:new.aid;
  
  END;
  
    CREATE OR REPLACE TRIGGER recalc_institution_update_count AFTER
  UPDATE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article SET institution_count = institution_count - 1 WHERE aid=:old.aid;
      UPDATE z_article SET institution_count = institution_count + 1 WHERE aid=:new.aid;
  
  END;
  
    
    CREATE OR REPLACE TRIGGER recalc_institution_delete_count AFTER
  DELETE ON z_work
  FOR EACH ROW
  DECLARE
    v_count NUMBER;
  BEGIN

      UPDATE z_article SET institution_count = institution_count - 1 WHERE aid=:old.aid;
  
  END;

