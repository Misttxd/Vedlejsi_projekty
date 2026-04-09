-- A

-- 1
CREATE OR REPLACE FUNCTION F_SetArticleInstitution(
    p_aid INT,
    p_iid INT,
    p_set BOOLEAN
) RETURN CHAR IS
    v_exists INT;
BEGIN
    SELECT COUNT(*) INTO v_exists 
    FROM z_article_institution 
    WHERE aid = p_aid AND iid = p_iid;
    
    IF p_set THEN
        IF v_exists = 0 THEN
            INSERT INTO z_article_institution (aid, iid) VALUES (p_aid, p_iid);
            UPDATE z_article SET institution_count = institution_count + 1 WHERE aid = p_aid;
            RETURN 'I';
        END IF;
    ELSE
        IF v_exists > 0 THEN
            DELETE FROM z_article_institution WHERE aid = p_aid AND iid = p_iid;
            UPDATE z_article SET institution_count = institution_count - 1 WHERE aid = p_aid;
            RETURN 'D';
        END IF;
    END IF;
    RETURN 'N';
END;

-- 2
CREATE OR REPLACE FUNCTION F_ExportArticleXML(
    p_aid INT
) RETURN CLOB IS
    v_xml CLOB;
    v_name VARCHAR2(1000);
    v_year INT;
    v_type VARCHAR2(40);
BEGIN
    SELECT name, year, type INTO v_name, v_year, v_type FROM z_article WHERE aid = p_aid;
    
    v_xml := '<article aid="' || p_aid || '">' ||
              '<name>' || v_name || '</name>' ||
              '<year>' || v_year || '</year>' ||
              '<type>' || v_type || '</type>' ||
              '<institutions>';
    
    FOR inst IN (SELECT i.iid, i.name FROM z_institution i 
                 JOIN z_article_institution ai ON i.iid = ai.iid WHERE ai.aid = p_aid) 
    LOOP
        v_xml := v_xml || '<institution iid="' || inst.iid || '"><name>' || inst.name || '</name></institution>';
    END LOOP;
    
    v_xml := v_xml || '</institutions><authors>';
    
    FOR auth IN (SELECT a.rid, a.name FROM z_author a 
                 JOIN z_article_author aa ON a.rid = aa.rid WHERE aa.aid = p_aid) 
    LOOP
        v_xml := v_xml || '<author rid="' || auth.rid || '"><name>' || auth.name || '</name></author>';
    END LOOP;
    
    v_xml := v_xml || '</authors></article>';
    RETURN v_xml;
END;

-- B

-- 1
CREATE OR REPLACE FUNCTION F_SetArticleAuthor(
    p_aid INT,
    p_rid INT,
    p_set BOOLEAN
) RETURN CHAR IS
    v_exists INT;
BEGIN
    SELECT COUNT(*) INTO v_exists 
    FROM z_article_author 
    WHERE aid = p_aid AND rid = p_rid;
    
    IF p_set THEN
        IF v_exists = 0 THEN
            INSERT INTO z_article_author (aid, rid) VALUES (p_aid, p_rid);
            UPDATE z_article SET author_count = author_count + 1 WHERE aid = p_aid;
            RETURN 'I';
        END IF;
    ELSE
        IF v_exists > 0 THEN
            DELETE FROM z_article_author WHERE aid = p_aid AND rid = p_rid;
            UPDATE z_article SET author_count = author_count - 1 WHERE aid = p_aid;
            RETURN 'D';
        END IF;
    END IF;
    RETURN 'N';
END;

-- 2
CREATE OR REPLACE FUNCTION F_ExportJournalXML(
    p_jid INT,
    p_year INT
) RETURN CLOB IS
    v_xml CLOB;
    v_name VARCHAR2(1000);
    v_issn VARCHAR2(20);
BEGIN
    SELECT name, issn INTO v_name, v_issn FROM z_journal WHERE jid = p_jid;
    
    v_xml := '<journal jid="' || p_jid || '"><name>' || v_name || '</name><issn>' || v_issn || '</issn><fields>';
    
    FOR fld IN (SELECT f.fid, f.name FROM z_field_ford f 
                JOIN z_year_field_journal yfj ON f.fid = yfj.fid
                WHERE yfj.jid = p_jid AND yfj.year = p_year) 
    LOOP
        v_xml := v_xml || '<field fid="' || fld.fid || '"><name>' || fld.name || '</name></field>';
    END LOOP;
    
    v_xml := v_xml || '</fields><articles>';
    
    FOR art IN (SELECT a.aid, a.name FROM z_article a 
                WHERE a.jid = p_jid AND a.year = p_year) 
    LOOP
        v_xml := v_xml || '<article aid="' || art.aid || '"><name>' || art.name || '</name></article>';
    END LOOP;
    
    v_xml := v_xml || '</articles></journal>';
    RETURN v_xml;
END;