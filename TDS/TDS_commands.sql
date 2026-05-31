SET DEFINE OFF;
SET SERVEROUTPUT ON;
ALTER SESSION SET NLS_DATE_LANGUAGE = ENGLISH;

/* ============================================================================
   TDS I - Rhythm game database
   File: TDS_commands.sql

   Student version: SQL commands are kept as close as possible to the
   code snippets in projekt_latex.tex.
   ============================================================================ */

/* ============================================================================
   DD S15 L01
   Assignment: NapiĹˇte dotaz pro spojovĂˇnĂ­ Ĺ™etÄ›zcĹŻ pomocĂ­ ||, pomocĂ­ CONCAT(). SELECT DISTINCT
   ============================================================================ */

SELECT DISTINCT u.username || ' - ' || p.display_name AS full_identifier, 
       CONCAT('Email: ', u.email) AS contact_info
FROM tds_users u JOIN tds_players p ON u.user_id = p.user_id;

/* ============================================================================
   DD S16 L02
   Assignment: WHERE podmĂ­nky pro vĂ˝bÄ›r Ĺ™Ă­dkĹŻ. Funkce LOWER, UPPER, INITCAP
   ============================================================================ */

SELECT UPPER(title) AS song_title, INITCAP(artist) AS artist_name, LOWER(difficulty_name) AS diff
FROM tds_songs s JOIN tds_song_variants v ON s.song_id = v.song_id
WHERE bpm > 150;

/* ============================================================================
   DD S16 L03
   Assignment: BETWEEN ... AND, LIKE (%,_), IN(), IS NULL, IS NOT NULL
   ============================================================================ */

SELECT * FROM tds_game_sessions 
WHERE accuracy BETWEEN 90.0 AND 100.0 
  AND max_combo IS NOT NULL 
  AND user_id IN (1, 2, 3);

SELECT * FROM tds_users WHERE email LIKE '%@rhythm.cz';

/* ============================================================================
   DD S17 L01
   Assignment: AND, OR, NOT. Priorita vyhodnocenĂ­ pomocĂ­ ()
   ============================================================================ */

SELECT * FROM tds_song_variants 
WHERE (note_count > 1000 AND max_score > 100000) OR NOT difficulty_name = 'Easy';

/* ============================================================================
   DD S17 L02
   Assignment: ORDER BY atr [ASC/DESC]. TĹ™Ă­dÄ›nĂ­ podle jednoho nebo vice atributĹŻ
   ============================================================================ */

SELECT * FROM tds_game_sessions 
ORDER BY score DESC, accuracy DESC, played_at ASC;

/* ============================================================================
   DD S17 L03
   Assignment: JednoĹ™ĂˇdkovĂ© funkce. VĂ­ceĹ™ĂˇdkovĂ© funkce MIN, MAX, AVG, SUM, COUNT
   ============================================================================ */

SELECT user_id, COUNT(session_id) as games_played, MAX(score) as best_score, 
       MIN(accuracy) as worst_acc, AVG(accuracy) as avg_acc, SUM(max_combo) as total_combo
FROM tds_game_sessions
GROUP BY user_id;

/* ============================================================================
   SQL S01 L01
   Assignment: LOWER, UPPER, INITCAP. CONCAT, SUBSTR, LENGTH, INSTR, LPAD, RPAD, TRIM, REPLACE. PouĹľijte tabulku DUAL
   ============================================================================ */

SELECT SUBSTR(username, 1, 5) AS short_name, LENGTH(email) AS email_len, 
       INSTR(email, '@') AS at_position, LPAD(role, 5, '*') AS padded_role,
       RPAD(role, 5, '*') AS padded_role_right,
       LOWER(username) AS username_lower,
       UPPER(email) AS email_upper,
       INITCAP(username) AS username_initcap,
       CONCAT(username, '_player') AS concat_example,
       REPLACE(email, 'rhythm.cz', 'game.com') AS new_email
FROM tds_users;

SELECT TRIM('  Rhythm Game  ') AS trimmed FROM DUAL;

/* ============================================================================
   SQL S01 L02
   Assignment: ROUND, TRUNC zaukrouhlenĂ­ na 2 desetinnĂˇ mĂ­sta, na celĂ© tisĂ­ce MOD
   ============================================================================ */

SELECT score,
       ROUND(accuracy, 2) AS acc_rounded_2,
       TRUNC(accuracy, 2) AS acc_truncated_2,
       ROUND(score, -3) AS score_rounded_thousands,
       TRUNC(score, -3) AS score_truncated_thousands,
       MOD(max_combo, 10) AS combo_mod_10
FROM tds_game_sessions;

/* ============================================================================
   SQL S01 L03
   Assignment: MONTHS_BETWEEN, ADD_MONTHS, NEXT_DAY, LAST_DAY, ROUND, TRUNC. SystĂ©movĂˇ konstanta SYSDATE
   ============================================================================ */

SELECT created_at, ADD_MONTHS(created_at, 6) AS mid_anniversary, 
       LAST_DAY(created_at) AS end_of_month, 
       NEXT_DAY(SYSDATE, 'MONDAY') AS next_monday,
       ROUND(SYSDATE, 'MONTH') AS rounded_month,
       TRUNC(SYSDATE, 'YEAR') AS year_start,
       MONTHS_BETWEEN(SYSDATE, created_at) AS months_active
FROM tds_users;

/* ============================================================================
   SQL S02 L01
   Assignment: TO_CHAR, TO_NUMBER, TO_DATE
   ============================================================================ */

SELECT TO_CHAR(played_at, 'DD.MM.YYYY HH24:MI') AS formatted_date,
       TO_CHAR(score, '999,999,999') AS formatted_score,
       TO_NUMBER('12345') AS parsed_number
FROM tds_game_sessions;

SELECT TO_DATE('01.01.2024', 'DD.MM.YYYY') AS parsed_date FROM DUAL;

/* ============================================================================
   SQL S02 L02
   Assignment: NVL, NVL2, NULLIF, COALESCE
   ============================================================================ */

SELECT comment_id, NVL(parent_comment_id, 0) AS safe_parent_id,
       NVL2(parent_comment_id, 'Je to odpoved', 'Hlavni komentar') AS comment_type,
       NULLIF(parent_comment_id, comment_id) AS parent_if_different,
       COALESCE(parent_comment_id, comment_id) AS thread_id
FROM tds_comments;

/* ============================================================================
   SQL S02 L03
   Assignment: DECODE, CASE, IF-THEN-ELSE
   ============================================================================ */

SELECT username, 
       CASE role 
           WHEN 'A' THEN 'Administrator' 
           WHEN 'P' THEN 'Player' 
           ELSE 'Unknown' 
       END AS role_desc,
       DECODE(role, 'A', 1, 0) AS is_admin_flag
FROM tds_users;

/* ============================================================================
   SQL S03 L01
   Assignment: NATURAL JOIN, CROSS JOIN
   ============================================================================ */

-- CROSS JOIN: every player combined with every song, useful for generated statistics
SELECT p.display_name, s.title 
FROM tds_players p CROSS JOIN tds_songs s;

-- NATURAL JOIN: joins by common columns, for example user_id
SELECT * FROM tds_users NATURAL JOIN tds_players;

/* ============================================================================
   SQL S03 L02
   Assignment: JOIN ... USING(atr), JOIN.. ON (podmĂ­nka spojenĂ­)
   ============================================================================ */

SELECT u.username, p.display_name 
FROM tds_users u JOIN tds_players p USING (user_id);

SELECT gs.score, sv.difficulty_name 
FROM tds_game_sessions gs JOIN tds_song_variants sv ON gs.variant_id = sv.variant_id;

/* ============================================================================
   SQL S03 L03
   Assignment: LEFT OUTER JOIN ... ON (), RIGHT OUTER JOIN ... ON (), FULL OUTER JOIN ... ON ()
   ============================================================================ */

-- LEFT JOIN: all songs and their variants, including songs without variants
SELECT s.title, v.difficulty_name 
FROM tds_songs s LEFT OUTER JOIN tds_song_variants v ON s.song_id = v.song_id;

-- RIGHT JOIN: all achievements and optionally players who have them
SELECT a.title, ua.user_id 
FROM tds_user_achievements ua RIGHT OUTER JOIN tds_achievements a ON ua.achievement_id = a.achievement_id;

-- FULL JOIN: combination of both sides
SELECT u.username, p.display_name 
FROM tds_users u FULL OUTER JOIN tds_players p ON u.user_id = p.user_id;

/* ============================================================================
   SQL S03 L04
   Assignment: SpojovĂˇnĂ­ 2x stejnĂ© tabulky s pĹ™ejmenovĂˇnĂ­m (vazba mezi nadĹ™Ă­zenĂ˝mi a podĹ™Ă­zenĂ˝mi v jednĂ© tabulce). HierarchickĂ© dotazovĂˇnĂ­ -- stromovĂˇ struktura zanoĹ™enĂ­ START WITH, CONNECT BY PRIOR, LEVEL
   ============================================================================ */

-- Self-join
SELECT c1.comment_text AS Reply, c2.comment_text AS Original
FROM tds_comments c1 JOIN tds_comments c2 ON c1.parent_comment_id = c2.comment_id;

-- Hierarchical query: comment tree
SELECT LEVEL, LPAD(' ', 2*(LEVEL-1)) || comment_text AS tree_comment
FROM tds_comments
START WITH parent_comment_id IS NULL
CONNECT BY PRIOR comment_id = parent_comment_id;

/* ============================================================================
   SQL S04 L02
   Assignment: AVG, COUNT, MIN, MAX, SUM, VARIANCE, STDDEV
   ============================================================================ */

SELECT variant_id, COUNT(*) AS play_count,
       MIN(score) AS min_score, MAX(score) AS max_score,
       AVG(accuracy) AS avg_acc, SUM(score) AS total_score,
       VARIANCE(accuracy) AS acc_var, STDDEV(accuracy) AS acc_stddev
FROM tds_game_sessions
GROUP BY variant_id;

/* ============================================================================
   SQL S04 L03
   Assignment: COUNT, COUNT(DISTINCT), NVL. RozdĂ­l mezi COUNT (*) a COUNT (atribut). ProÄŤ NVL u agregaÄŤnĂ­ch funkcĂ­
   ============================================================================ */

-- COUNT(*) counts all rows, COUNT(parent_comment_id) counts only non-NULL parent IDs
SELECT COUNT(*) AS total_comments, 
       COUNT(parent_comment_id) AS total_replies,
       COUNT(DISTINCT user_id) AS unique_commenters,
       AVG(NVL(parent_comment_id, 0)) AS avg_parent_id_safe
FROM tds_comments;

/* ============================================================================
   SQL S05 L01
   Assignment: GROUP BY, HAVING
   ============================================================================ */

SELECT variant_id, COUNT(*) as plays, AVG(score) as avg_score
FROM tds_game_sessions
GROUP BY variant_id
HAVING COUNT(*) > 0 AND AVG(score) > 10000;

/* ============================================================================
   SQL S05 L02
   Assignment: ROLLUP, CUBE, GROUPING SETS
   ============================================================================ */

-- Subtotals by user and variant, including the overall total
SELECT user_id, variant_id, SUM(score)
FROM tds_game_sessions
GROUP BY ROLLUP(user_id, variant_id);

SELECT user_id, variant_id, SUM(score)
FROM tds_game_sessions
GROUP BY CUBE(user_id, variant_id);

SELECT user_id, variant_id, SUM(score)
FROM tds_game_sessions
GROUP BY GROUPING SETS ((user_id), (variant_id), ());

/* ============================================================================
   SQL S05 L03
   Assignment: MnoĹľinovĂ© operace v SQL -- UNION, UNION ALL, INTERSECT, MINUS. ORDER BY u mnoĹľinovĂ˝ch operacĂ­
   ============================================================================ */

-- Returns unique IDs of users who played a game or wrote comments
SELECT user_id FROM tds_game_sessions
UNION
SELECT user_id FROM tds_comments
ORDER BY user_id DESC;

SELECT user_id FROM tds_game_sessions
UNION ALL
SELECT user_id FROM tds_comments;

SELECT user_id FROM tds_game_sessions
INTERSECT
SELECT user_id FROM tds_comments;

-- Returns users who played a game but never wrote a comment
SELECT user_id FROM tds_game_sessions
MINUS
SELECT user_id FROM tds_comments;

/* ============================================================================
   SQL S06 L01
   Assignment: VnoĹ™enĂ© dotazy. VĂ˝sledek jako jedinĂˇ hodnota. VĂ­cesloupcovĂ˝ poddotaz. EXISTS, NOT EXISTS
   ============================================================================ */

-- EXISTS example
SELECT display_name FROM tds_players p
WHERE EXISTS (SELECT 1 FROM tds_game_sessions s WHERE s.user_id = p.user_id);

-- Scalar subquery
SELECT username FROM tds_users
WHERE user_id = (
    SELECT user_id FROM tds_game_sessions
    WHERE score = (SELECT MAX(score) FROM tds_game_sessions)
);

-- Multi-column subquery: find sessions with the same score and combo as session 1
SELECT * FROM tds_game_sessions
WHERE (score, max_combo) = (SELECT score, max_combo FROM tds_game_sessions WHERE session_id = 1);

-- NOT EXISTS
SELECT display_name FROM tds_players p
WHERE NOT EXISTS (SELECT 1 FROM tds_comments c WHERE c.user_id = p.user_id);

/* ============================================================================
   SQL S06 L02
   Assignment: JednoĹ™ĂˇdkovĂ© poddotazy
   ============================================================================ */

-- Player/session with the highest score overall
SELECT user_id, score FROM tds_game_sessions
WHERE score = (SELECT MAX(score) FROM tds_game_sessions);

/* ============================================================================
   SQL S06 L03
   Assignment: VĂ­ceĹ™ĂˇdkovĂ© poddotazy IN, ANY, ALL. NULL hodnoty v poddotazech
   ============================================================================ */

-- Players who played a variant harder than Easy
SELECT display_name FROM tds_players
WHERE user_id IN (
    SELECT user_id FROM tds_game_sessions 
    WHERE variant_id IN (SELECT variant_id FROM tds_song_variants WHERE difficulty_name != 'Easy')
);

SELECT score FROM tds_game_sessions WHERE score > ALL (SELECT required_points FROM tds_achievements);

SELECT score FROM tds_game_sessions WHERE score > ANY (SELECT required_points FROM tds_achievements);

SELECT comment_id, comment_text
FROM tds_comments
WHERE parent_comment_id NOT IN (
    SELECT parent_comment_id
    FROM tds_comments
    WHERE parent_comment_id IS NOT NULL
);

/* ============================================================================
   SQL S06 L04
   Assignment: WITH.. AS() konstrukce poddotazu
   ============================================================================ */

WITH PlayerStats AS (
    SELECT user_id, AVG(accuracy) as avg_acc, SUM(score) as total_score
    FROM tds_game_sessions
    GROUP BY user_id
)
SELECT p.display_name, ps.avg_acc, ps.total_score
FROM tds_players p JOIN PlayerStats ps ON p.user_id = ps.user_id
WHERE ps.avg_acc > 90;

/* ============================================================================
   SQL S07 L01
   Assignment: INSERT INTO Tab VALUES(). INSERT INTo Tab (atr, atr) VALUES(). INSERT INTO Tab AS SELECT ...
   ============================================================================ */

INSERT INTO tds_achievements
VALUES (99, 'Practice Day', 'Play any chart during practice.', 0);

INSERT INTO tds_achievements (achievement_id, title, description, required_points)
VALUES (100, 'Night Session', 'Play after midnight.', 1000);

-- Insert data from a SELECT statement, for example for archiving
CREATE TABLE tds_archived_sessions AS SELECT * FROM tds_game_sessions WHERE 1=0;
INSERT INTO tds_archived_sessions SELECT * FROM tds_game_sessions WHERE played_at < SYSDATE - 365;

/* ============================================================================
   SQL S07 L02
   Assignment: UPDATE Tab SET atr = ... WHERE podm. DELETE FROM Tab WHERE atr = ...
   ============================================================================ */

UPDATE tds_players SET total_points = total_points + 100 WHERE level_number < 10;

DELETE FROM tds_comments WHERE comment_text LIKE '%delete-me%';

/* ============================================================================
   SQL S07 L03
   Assignment: DEFAULT, MERGE, Multi-Table Inserts
   ============================================================================ */

INSERT INTO tds_users (user_id, username, email, password_hash)
VALUES (50, 'DefaultRoleUser', 'default.role@rhythm.cz', 'hash50');

INSERT INTO tds_players (user_id, display_name)
VALUES (50, 'Default Role User');

MERGE INTO tds_players p
USING (SELECT user_id, SUM(score) as s FROM tds_game_sessions GROUP BY user_id) s
ON (p.user_id = s.user_id)
WHEN MATCHED THEN UPDATE SET p.total_points = p.total_points + s.s;

INSERT ALL
  INTO tds_achievements (achievement_id, title, description, required_points)
      VALUES (101, 'Inserted A', 'Inserted by multi-table insert.', 0)
  INTO tds_achievements (achievement_id, title, description, required_points)
      VALUES (102, 'Inserted B', 'Inserted by multi-table insert.', 0)
SELECT * FROM DUAL;

/* ============================================================================
   SQL S08 L01
   Assignment: Objekty v databĂˇzĂ­ -- Tabulky, Indexy, Constraint, View, Sequnce, Synonym. CREATE, ALTER, DROP, RENAME, TRUNCATE. CREATE TABLE (atr DAT TYP, DEFAULT NOT NULL). ORGANIZATION EXTERNAL, TYPE ORACLE_LOADER, DEFAULT DICTIONARY, ACESS PARAMETERS, RECORDS DELIMITED BY NEWLINE, FIELDS, LOCATION
   ============================================================================ */

CREATE TABLE tds_temp_logs (log_id NUMBER, text VARCHAR2(100));
ALTER TABLE tds_temp_logs ADD (log_date DATE);
RENAME tds_temp_logs TO tds_system_logs;
TRUNCATE TABLE tds_system_logs;
DROP TABLE tds_system_logs;

-- External table syntax: requires a DIRECTORY object in the database
/*
CREATE TABLE ext_songs (title VARCHAR2(100), bpm NUMBER)
ORGANIZATION EXTERNAL (
  TYPE ORACLE_LOADER DEFAULT DIRECTORY ext_dir
  ACCESS PARAMETERS (RECORDS DELIMITED BY NEWLINE FIELDS TERMINATED BY ',')
  LOCATION ('songs.csv')
);
*/

/* ============================================================================
   SQL S08 L02
   Assignment: TIMESTAMP, TIMESTAMP WITH TIME ZONE, TIMESTAM WITH LOCAL TIMEZONE. INTERVAL YEAT TO MONTH, INTERVAL DAY TO SECOND. CHAR, VARCHAR2, CLOB. NUMBER. BLOB
   ============================================================================ */

CREATE TABLE tds_datatypes_demo (
    id NUMBER,
    event_time TIMESTAMP WITH TIME ZONE,
    duration INTERVAL DAY TO SECOND,
    long_desc CLOB,
    binary_file BLOB
);

/* ============================================================================
   SQL S08 L03
   Assignment: ALTER TABLE (ADD, MODIFY, DROP), DROP, RENAME. FLASHBACK TABLE Tab TO BEFORE DROP (pohled USER_RECYCLEBIN). DELETE, TRUNCATE. COMMENT ON TABLE. SET UNUSED
   ============================================================================ */

COMMENT ON TABLE tds_game_sessions IS 'Stores data about each played song by a user';

CREATE TABLE tds_demo_alter (
    demo_id NUMBER,
    demo_text VARCHAR2(20)
);

ALTER TABLE tds_demo_alter ADD (demo_note VARCHAR2(50));
ALTER TABLE tds_demo_alter MODIFY (demo_text VARCHAR2(100));
ALTER TABLE tds_demo_alter DROP COLUMN demo_note;
ALTER TABLE tds_demo_alter SET UNUSED (demo_text);

-- Flashback example, if recycle bin is enabled
-- DROP TABLE tds_demo_alter;
-- FLASHBACK TABLE tds_demo_alter TO BEFORE DROP;

/* ============================================================================
   SQL S10 L01
   Assignment: CREATE TABLE (NOT NULL A UNIQUE constraint). CREATE TABLE Tab AS SELECT ... VlastnĂ­ vs. systĂ©movĂ© pojmenovĂˇnĂ­ CONSTRAINT podmĂ­nek
   ============================================================================ */

-- Custom constraint naming
CREATE TABLE tds_test (
    id NUMBER CONSTRAINT pk_tds_test PRIMARY KEY,
    name VARCHAR2(50) CONSTRAINT uq_tds_test UNIQUE
);

CREATE TABLE tds_player_copy AS
SELECT user_id, display_name, total_points
FROM tds_players;

/* ============================================================================
   SQL S10 L02
   Assignment: CONSTRAINT -- NOT NULL, UNIQUE, PRIMARY KEY, FOREIGN KEY (atr REFERENCES Tab(atr)), CHECK. CizĂ­ klĂ­ÄŤe, ON DELETE, ON UPDATE, RESTRICT, CASCADE atd.
   ============================================================================ */

-- These constraints are already applied in detail in the main DDL script
CREATE TABLE tds_demo_parent (
    parent_id NUMBER CONSTRAINT pk_tds_demo_parent PRIMARY KEY,
    parent_code VARCHAR2(20) CONSTRAINT uq_tds_demo_parent_code UNIQUE NOT NULL
);

CREATE TABLE tds_demo_child (
    child_id NUMBER CONSTRAINT pk_tds_demo_child PRIMARY KEY,
    parent_id NUMBER NOT NULL,
    child_score NUMBER CONSTRAINT chk_tds_demo_child_score CHECK (child_score >= 0),
    CONSTRAINT fk_tds_demo_child_parent FOREIGN KEY (parent_id)
        REFERENCES tds_demo_parent(parent_id) ON DELETE CASCADE
);

DROP TABLE tds_demo_child PURGE;
DROP TABLE tds_demo_parent PURGE;

/* ============================================================================
   SQL S10 L03
   Assignment: USER_CONSTRAINTS
   ============================================================================ */

SELECT constraint_name, constraint_type, table_name 
FROM user_constraints 
WHERE table_name = 'TDS_USERS';

/* ============================================================================
   SQL S11 L01
   Assignment: CREATE VIEW. FORCE, NOFORCE. WITCH CHECK OPTION. WITH READ ONLY. Simple vs. Compex VIEW
   ============================================================================ */

CREATE OR REPLACE NOFORCE VIEW v_player_summary AS
SELECT user_id, display_name, total_points, level_number
FROM tds_players;

CREATE OR REPLACE FORCE VIEW v_high_scores AS
SELECT session_id, user_id, score, accuracy
FROM tds_game_sessions
WHERE accuracy >= 90
WITH CHECK OPTION;

CREATE OR REPLACE VIEW v_top_players AS
SELECT p.display_name, p.total_points, p.level_number
FROM tds_players p
WHERE p.total_points > 1000
WITH READ ONLY;

/* ============================================================================
   SQL S11 L03
   Assignment: INLINE VIEW Poddotaz v podobÄ› tabulky SELECT atr FROM (SELECT * FROM Tab) alt_tab
   ============================================================================ */

-- Inline View
SELECT top_players.display_name
FROM (SELECT display_name, total_points FROM tds_players ORDER BY total_points DESC) top_players
WHERE ROWNUM <= 5;

/* ============================================================================
   SQL S12 L01
   Assignment: CREATE SEQUENCE nazev INCREMENT BY n START WITH m, (NO)MAXVALUE, (NO)MINVALUE, (NO)CYCLE, (NO)CACHE. ALTER SEQUENCE
   ============================================================================ */

CREATE SEQUENCE seq_tds_custom START WITH 100 INCREMENT BY 5 NOCACHE NOCYCLE;

ALTER SEQUENCE seq_tds_custom INCREMENT BY 10 NOCACHE NOCYCLE;

/* ============================================================================
   SQL S12 L02
   Assignment: CREATE INDEX, PRIMARY KEY, UNIQUE KEY, FOREINGN KEY
   ============================================================================ */

CREATE INDEX idx_tds_game_sessions_acc ON tds_game_sessions(accuracy DESC);

/* ============================================================================
   SQL S13 L01
   Assignment: GRANT ... ON ... TO ... PUBLIC. REVOKE. JakĂˇ prĂˇva lze pĹ™idÄ›lit na jakĂ© objekty? (ALTER, DELETE, EXECUTE, INDEX, INSERT, REFERENCES, SELECT, UPDATE) -- (TABLE, VIEW, SEQUENCE, PROCEDURE)
   ============================================================================ */

GRANT SELECT, INSERT, DELETE ON tds_game_sessions TO PUBLIC;
REVOKE DELETE ON tds_game_sessions FROM PUBLIC;

/* ============================================================================
   SQL S13 L03
   Assignment: RegulĂˇrnĂ­ vĂ˝razy. REGEXP_LIKE, REGEXP_REPLACE, REGEXP_INSTR, REGEXP_SUBSTR, REGEXP_COUNT
   ============================================================================ */

-- Finds e-mails containing the word rhythm
SELECT email FROM tds_users WHERE REGEXP_LIKE(email, '^.*rhythm.*$');

SELECT REGEXP_REPLACE(email, '@.*', '@hidden.com') AS masked_email FROM tds_users;

SELECT title,
       REGEXP_INSTR(title, '[[:alpha:]]+') AS first_word_position,
       REGEXP_SUBSTR(title, '[[:alpha:]]+') AS first_word,
       REGEXP_COUNT(title, '[[:alpha:]]+') AS word_count
FROM tds_songs;

/* ============================================================================
   SQL S14 L01
   Assignment: Transakce, COMMIT, ROLLBACK, SAVEPOINT
   ============================================================================ */

UPDATE tds_players SET total_points = 0;
SAVEPOINT before_delete;
DELETE FROM tds_players;
ROLLBACK TO before_delete;
COMMIT;

/* ============================================================================
   SQL S15 L01
   Assignment: AlternativnĂ­ zĂˇpis spojovĂˇnĂ­ bez JOIN s podmĂ­nkou spojenĂ­ ve WHERE. LevĂ© a pravĂ© spojenĂ­ s pomocĂ­ atrA = atrB (+)
   ============================================================================ */

-- Old Oracle-style left outer join
SELECT s.title, gs.score
FROM tds_songs s, tds_song_variants v, tds_game_sessions gs
WHERE s.song_id = v.song_id(+)
  AND v.variant_id = gs.variant_id(+);

-- Old Oracle-style right outer join
SELECT u.username, p.display_name
FROM tds_users u, tds_players p
WHERE u.user_id(+) = p.user_id;

/* ============================================================================
   SQL S16 L03
   Assignment: Rekapitulace pĹ™Ă­kazĹŻ a parametrĹŻ -- vĹˇe co nebylo uvedeno v pĹ™edchozĂ­ch bodech dodÄ›lat zde
   ============================================================================ */

-- Complex recap query:
-- joins users and their game sessions, calculates statistics for the last year,
-- and filters only users with activity.
SELECT 
    u.username,
    COUNT(gs.session_id) AS total_plays,
    MAX(gs.score) AS highest_score,
    ROUND(AVG(gs.accuracy), 2) AS average_accuracy
FROM tds_users u
JOIN tds_game_sessions gs ON u.user_id = gs.user_id
WHERE gs.played_at >= ADD_MONTHS(SYSDATE, -12)
GROUP BY u.username
HAVING COUNT(gs.session_id) > 0
ORDER BY highest_score DESC;

COMMIT;
