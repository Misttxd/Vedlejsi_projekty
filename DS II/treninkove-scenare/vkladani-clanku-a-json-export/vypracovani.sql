-- A
-- 1

CREATE OR REPLACE PROCEDURE P_InsertArticle(
    p_issn VARCHAR,
    p_inst_name VARCHAR,
    p_author_name VARCHAR,
    p_year INT,
    p_article_name VARCHAR
) AS
    v_jid INT;
    v_iid INT;
    v_rid INT;
    v_aid INT;
BEGIN
    BEGIN
        SELECT jid INTO v_jid FROM z_journal WHERE issn = p_issn;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Časopis nenalezen.');
        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(-20002, 'Více časopisů odpovídá ISSN.');
    END;
    
    BEGIN
        SELECT iid INTO v_iid FROM z_institution WHERE name = p_inst_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20003, 'Instituce nenalezena.');
        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(-20004, 'Více institucí odpovídá jménu.');
    END;
    
    BEGIN
        SELECT rid INTO v_rid FROM z_author WHERE name = p_author_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20005, 'Autor nenalezen.');
        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(-20006, 'Více autorů odpovídá jménu.');
    END;
    
    SELECT COALESCE(MAX(aid), 0) + 1 INTO v_aid FROM z_article;
    INSERT INTO z_article (aid, jid, name, year, author_count, institution_count) 
    VALUES (v_aid, v_jid, p_article_name, p_year, 0, 0);
    
    INSERT INTO z_article_institution (aid, iid) VALUES (v_aid, v_iid);
    UPDATE z_article SET institution_count = institution_count + 1 WHERE aid = v_aid;
    
    INSERT INTO z_article_author (aid, rid) VALUES (v_aid, v_rid);
    UPDATE z_article SET author_count = author_count + 1 WHERE aid = v_aid;
END;

-- 2
CREATE OR REPLACE FUNCTION F_ExportArticleJSON(
    p_aid INT
) RETURN CLOB IS
    v_json CLOB;
    v_name VARCHAR2(1000);
    v_year INT;
    v_type VARCHAR2(40);
BEGIN
    SELECT name, year, type INTO v_name, v_year, v_type FROM z_article WHERE aid = p_aid;
    
    v_json := '{' ||
              '"aid": ' || p_aid || ',' ||
              '"name": "' || v_name || '",' ||
              '"year": ' || v_year || ',' ||
              '"type": "' || v_type || '",' ||
              '"institutions": [';
    
    FOR inst IN (SELECT i.iid, i.name FROM z_institution i 
                 JOIN z_article_institution ai ON i.iid = ai.iid WHERE ai.aid = p_aid) 
    LOOP
        v_json := v_json || '{"iid": ' || inst.iid || ', "name": "' || inst.name || '"},';
    END LOOP;

    -- Odstranění poslední čárky, pokud existují instituce
    IF v_json LIKE '%"institutions": [%,' THEN
        v_json := RTRIM(v_json, ',');
    END IF;
    
    v_json := v_json || '], "authors": [';
    
    FOR auth IN (SELECT a.rid, a.name FROM z_author a 
                 JOIN z_article_author aa ON a.rid = aa.rid WHERE aa.aid = p_aid) 
    LOOP
        v_json := v_json || '{"rid": ' || auth.rid || ', "name": "' || auth.name || '"},';
    END LOOP;

    -- Odstranění poslední čárky, pokud existují autoři
    IF v_json LIKE '%"authors": [%,' THEN
        v_json := RTRIM(v_json, ',');
    END IF;
    
    v_json := v_json || ']}';
    RETURN v_json;
END;

-- B

-- 1
CREATE OR REPLACE PROCEDURE P_SetRanking(
    p_ff_name VARCHAR,
    p_issn VARCHAR,
    p_year INT,
    p_ranking VARCHAR
) AS
    v_fid INT;
    v_jid INT;
    v_exists INT;
BEGIN
    BEGIN
        SELECT fid INTO v_fid FROM z_field_ford WHERE name = p_ff_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20005, 'Field ford nenalezen.');
        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(-20006, 'Více oborů Field ford odpovídá názvu.');
    END;

    BEGIN
        SELECT jid INTO v_jid FROM z_journal WHERE issn = p_issn;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Časopis nenalezen.');
        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(-20002, 'Více časopisů odpovídá ISSN.');
    END;
    
    SELECT COUNT(*) INTO v_exists FROM z_year_field_journal WHERE fid = v_fid AND jid = v_jid AND year = p_year;
    
    IF p_ranking != 0 THEN
        IF v_exists > 0 THEN
            UPDATE z_year_field_journal SET ranking = p_ranking WHERE fid = v_fid AND jid = v_jid AND year = p_year;
        ELSE
            INSERT INTO z_year_field_journal (fid, jid, year, ranking) VALUES (v_fid, v_jid, p_year, p_ranking);
        END IF;
    ELSE
        DELETE FROM z_year_field_journal WHERE fid = v_fid AND jid = v_jid AND year = p_year;
    END IF;
END;

-- 2
CREATE OR REPLACE FUNCTION F_ExportJournalJSON(
    p_jid INT,
    p_year INT
) RETURN CLOB IS
    v_json CLOB;
    v_name VARCHAR2(1000);
    v_issn VARCHAR2(20);
BEGIN
    SELECT name, issn INTO v_name, v_issn FROM z_journal WHERE jid = p_jid;
    
    v_json := '{"jid":' || p_jid || ', "name":"' || v_name || '", "issn":"' || v_issn || '", "fields": [';
    
    FOR fld IN (SELECT f.fid, f.name FROM z_field_ford f 
                JOIN z_year_field_journal yfj ON f.fid = yfj.fid
                WHERE yfj.jid = p_jid AND yfj.year = p_year) 
    LOOP
        v_json := v_json || '{"fid":' || fld.fid || ', "name":"' || fld.name || '"},';
    END LOOP;
    
    v_json := RTRIM(v_json, ',') || '], "articles": [';
    
    FOR art IN (SELECT a.aid, a.name FROM z_article a 
                WHERE a.jid = p_jid AND a.year = p_year) 
    LOOP
        v_json := v_json || '{"aid":' || art.aid || ', "name":"' || art.name || '"},';
    END LOOP;
    
    v_json := RTRIM(v_json, ',') || ']}';
    RETURN v_json;
END;