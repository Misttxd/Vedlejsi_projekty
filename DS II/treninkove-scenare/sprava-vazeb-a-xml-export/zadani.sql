CREATE OR REPLACE FUNCTION F_SetArticleInstitution (p_aid z_article_institution.aid%TYPE, p_iid z_article_institution.iid%TYPE, p_set BOOLEAN) RETURN CHAR AS
    v_count NUMBER;
BEGIN
    select count(*) into v_count
    from Z_ARTICLE_INSTITUTION ai
    where ai.AID = p_aid and ai.iid = p_iid;


    IF p_set = TRUE and v_count = 0 THEN

        INSERT INTO Z_ARTICLE_INSTITUTION (aid, iid)
        VALUES (p_aid, p_iid);

        UPDATE Z_ARTICLE
        SET institution_count = institution_count + 1
        WHERE AID = p_aid;

        RETURN 'I';



    ELSIF p_set = FALSE and v_count = 1 THEN

        DELETE FROM z_article_institution
        where aid = p_aid and iid = p_iid;

        UPDATE Z_ARTICLE
        SET institution_count = institution_count - 1
        WHERE AID = p_aid;

        return 'D';
    ELSE 
        RETURN 'N';
    END IF;
END;