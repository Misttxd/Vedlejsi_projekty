CREATE OR REPLACE PROCEDURE P_InsertArticle(v_issn z_Journal.issn%TYPE, v_InstitutionName z_Institution.name%TYPE, v_AuthorName z_Author.name%TYPE, v_year z_Article.year%TYPE, v_name z_Article.name%TYPE) AS

temp NUMBER :=0;
v_jid NUMBER := 0;
v_iid NUMBER := 0;
v_rid NUMBER := 0;

v_new_aid NUMBER := 0;

BEGIN

        select count(*) into temp
        from Z_JOURNAL j
        where j.issn = v_issn;

        if temp = 0 or temp > 1 THEN
            dbms_output.put_line('Časopis s daným issn neexistuje');
            RETURN;
        end if;

    select j.jid into v_jid
    from Z_JOURNAL j
    where j.issn = v_issn;


        select count(*) into temp
        from z_Institution i
        where i.name = v_InstitutionName;

        if temp = 0 or temp > 1 THEN
            dbms_output.put_line('Instituce s daným názvem neexistuje');
            RETURN;
        end if;

    select i.iid into v_iid
    from Z_INSTITUTION i
    where i.name = v_InstitutionName;

        select count(*) into temp
        from Z_AUTHOR a
        where a.name = v_AuthorName;

        if temp = 0 or temp > 1 THEN
            dbms_output.put_line('Autor s daným názvem neexistuje');
            RETURN;
        end if;

    select a.rid into v_rid
    from Z_AUTHOR a
    where a.name = v_AuthorName;

    --tohle je vlastně else, tadkže zbytek kódu může být pak ta akce co se stane v přivetimev stavu
    select NVL(MAX(aid), 0) + 1 into v_new_aid -- zajistí že když to bude NULL tak se to replacne za 0, 
    from z_Article a;

    INSERT INTO Z_ARTICLE(aid, jid, year, name)
    VALUES(v_new_aid, v_jid, v_year, v_name);

    INSERT INTO Z_ARTICLE_INSTITUTION(aid, iid) --BACHA NA TO TO ULOŽIT I DO TĚCHTO SPOJOVACÍCH TABULEK
    VALUES (v_new_aid, v_iid);

    INSERT INTO Z_ARTICLE_AUTHOR(aid, rid) --BACHA NA TO TO ULOŽIT I DO TĚCHTO SPOJOVACÍCH TABULEK
    VALUES (v_new_aid, v_rid);

END;