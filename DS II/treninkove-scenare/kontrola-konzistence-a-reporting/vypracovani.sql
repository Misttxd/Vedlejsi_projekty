VARIANTA A

1,
ALTER TABLE z_article
ADD article_author_count INTEGER;

ALTER TABLE z_article
ADD article_institution_count INTEGER;

UPDATE z_article
SET article_author_count = author_count,
    article_institution_count = institution_count;


CREATE OR REPLACE FUNCTION StatisticsConsistent(p_processType varchar)
RETURN BOOLEAN
AS
  v_authors_cnt integer;
  v_institutions_cnt integer;
BEGIN
  IF (p_processType != 'Analyze' AND p_processType != 'Fix') THEN
    RAISE_APPLICATION_ERROR(-20001, 'Invalid process type: ' || p_processType);
  END IF;
  
  FOR v_article IN (SELECT * FROM z_article) LOOP
    SELECT count(*) into v_authors_cnt FROM z_article_author WHERE  aid = v_article.aid;
    SELECT count(*) into v_institutions_cnt FROM z_article_institution WHERE  aid = v_article.aid;
    IF (v_authors_cnt != v_article.article_author_count or v_institutions_cnt != v_article.article_institution_count) THEN
       IF (p_processType = 'Analyze') THEN
          return false;
       ELSE
          update z_article set article_author_count = v_authors_cnt, article_institution_count = v_institutions_cnt where aid = v_article.aid;  
       END IF;
    END IF;
  END LOOP;
  
  return true;
EXCEPTION
  WHEN OTHERS THEN
    return false;
END;

SET SERVEROUTPUT ON;
BEGIN
  if (StatisticsConsistent('Analyze') = true) then
    DBMS_OUTPUT.PUT_LINE('Statistics are consistent');
  else
    DBMS_OUTPUT.PUT_LINE('Statistics are not consistent');  
  end if;
END;

2,
create or replace procedure ReportFord4Institution(p_institution_name z_institution.name%type)
as
  v_cnt int := 0;
  v_institution z_institution%rowtype;
begin
  select count(*) into v_cnt from z_institution where name=p_institution_name;
  
  if v_cnt = 0 then
    dbms_output.put_line('Instituce neexistuje.');
    return;
  elsif v_cnt > 1 then
    dbms_output.put_line('Nejednoznačný název instituce.');
    return;
  end if;

  select * into v_institution from z_institution where name=p_institution_name;
  
  dbms_output.put_line('Název instituce: ' || v_institution.name || ', id instituce: ' || v_institution.iid || ':' );
  v_cnt := 0;
  for item in (
    select distinct ff.name from z_article_institution ai
    join z_article a on ai.aid=a.aid
    join z_year_field_journal y on a.year=y.year and a.jid=y.jid
    join z_field_ford ff on y.fid=ff.fid
    where ai.iid=v_institution.iid
  )
  loop
    dbms_output.put_line(v_cnt ||'. ' || item.name);
    v_cnt := v_cnt + 1;
  end loop;
  
  dbms_output.put_line('Celkový počet oborů Ford článků instituce: ' || v_cnt);
end;


VARIANTA B

1,

create or replace function CopyArticleYear(p_rid z_author.rid%type, p_year int)
return int
as 
  v_cnt int;
  v_author z_author%rowtype;
  v_table_name varchar(1024);
begin

  select count(*) into v_cnt from z_author where rid=p_rid;
  
  if v_cnt = 0 then
    return -1;
  end if;
  
  select * into v_author from z_author where rid=p_rid;
  
  v_table_name := 'article_author_' || p_rid;
  
  dbms_output.put_line('Function outputs: ');
  dbms_output.put_line(v_author.name);
  dbms_output.put_line(v_table_name);

  execute immediate 'create table ' || v_table_name || '(' || 'aid int, last_update date)';
  
  execute immediate 'insert into article_author_' || p_rid || ' select a.aid, sysdate from z_article a ' ||
    'join z_article_author aa on a.aid=aa.aid ' ||
    'where aa.rid=:1 and a.year=:2' using p_rid, p_year;
    
  execute immediate 'select count(*) from article_author_' || p_rid into v_cnt;
  
  execute immediate 'drop table article_author_' || p_rid;
  
  return v_cnt;
end;

2,

CREATE TABLE z_institution_rank (
    iid INT NOT NULL,
    rank_order INT NOT NULL,
    PRIMARY KEY (iid),
    FOREIGN KEY (iid) REFERENCES z_institution(iid)
);


CREATE OR REPLACE PROCEDURE UpdateInstitutionRankOrder
AS
  v_order integer := 0;
  v_art_cnt integer := -1;
BEGIN
  BEGIN
    DELETE FROM z_institution_rank;

    -- Poznamka: je vhodne resit insert - select s rank over.
	
	FOR v_institution IN (select zi.iid, count(distinct zai.aid) art_cnt
                          from z_institution zi left join z_article_institution zai on zi.iid = zai.iid
                          group by zi.iid
                          order by count(distinct zai.aid) desc)
    LOOP
		if (v_art_cnt != v_institution.art_cnt) then
			v_order := v_order + 1;
		end if;	
        insert into z_institution_rank(iid, rank_order) values (v_institution.iid, v_order);    
        v_art_cnt := v_institution.art_cnt;
    END LOOP;
	COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
  END;
END; 