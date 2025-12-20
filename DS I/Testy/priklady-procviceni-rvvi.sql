/*
*

Příklady SQL dotazů pro databázi RVVI

 1. příklad: Vypište instituce se sídlem v městských částech Ostravy.

*/

select *
from z_institution i
where i.town like 'Ostrava%';

-- počet záznamů výsledku: 7

/*
2. příklad: Vypište instituce, které mají v názvu slovo ‘Ústav’

*/

select *
from z_institution i

where i.name like '%Ústav%';

-- počet záznamů výsledku: 102

/* 3. příklad: Vypište počet článků všech institucí se sídlem v městských
    částech Prahy. Pro každou instituci
vypište: id, název a město
    instituce a počet článků. Výsledek setřiďte sestupně dle počtu článků.

*/
select i.iid, i.name, i.town, count(ai.aid) as article_count
from z_institution i
         left join z_article_institution ai on i.iid = ai.iid
where i.town like 'Praha%'
group by i.iid, i.name, i.town
order by article_count desc;

-- počet záznamů výsledku: 122
/*
Poznámky: 1.

    Pokud by v zadání byl výpis pouze těch institucí, které mají nějaké
    články, pak použijeme (inner) join namísto left (outer) join.

 2.

    Není nutné spojovat tabulku z_article, počet článků institucí
    zjistíme z vazební tabulky z_article_institution.
*/


/*
 4. příklad: Vypište počet článků veřejných vysokých škol
    (z_institution.legal_form=’veřejná vysoká škola’) z roku 2021.
    Vypište pouze instituce, které měly v tomto roce nějaké články. Pro
    každou instituci
vypište: id, název a město instituce a počet
    článků. Výsledek setřiďte sestupně dle počtu článků.
*/
select i.iid, i.name, i.town, count(ai.aid) as article_count
from z_institution i
         join z_article_institution ai on i.iid = ai.iid
         join z_article a on ai.aid = a.aid
where i.legal_form = 'veřejná vysoká škola'
  and a.year = 2021
group by i.iid, i.name, i.town
order by article_count desc;

-- počet záznamů výsledku: 25
/*
Poznámky: 3.

    Zajímají nás jen instituce s nějakými články v roce 2021, použijeme
    tedy (inner) join namísto left (outer) join.

 4.

    Používáme selekci na atribut yeartabulky z_article, je tedy nutné
    připojit i tuto tabulku.
*/

/*
 5. příklad: Vypište počet článků autora 'Matas, Jiri'.

*/
select count(*) as article_count
from z_author ar
         join z_article_author aa on ar.rid = aa.rid
where ar.name = 'Matas, Jiri'

-- Výsledek: 12

/*
6. příklad: Vypište názvy článků a jejich časopisů autora 'Matas,
Jiri', kde jedna z institucí není 'České vysoké učení technické v
Praze'.
*/

select distinct a.name as article_name, j.name as journal_name
from z_article a
join z_article_author aa on a.aid=aa.aid
join z_author ar on ar.rid=aa.rid
join z_article_institution ai on ai.aid=a.aid
join z_institution i on ai.iid=i.iid
join z_journal j on a.jid=j.jid
where ar.name='Matas, Jiri' and not i.name='České vysoké učení technické
v Praze'

/*
Výsledek: article_namejournal_name

Restoration of Fast Moving Objectsieee transactions on image processing

Tracking by Deblattinginternational journal of computer vision


Poznámka: Jelikož inner join nám pro každý článek a instituci vytvoří ve
výsledku nový záznam, můžeme řešit jednoduše pomocí and not. Musíme
použít distinct, abychom odstranili záznamy případných ostatních institucí.
*/


/*
 7. příklad: Vypište všechny obory FORD (z_field_ford.name), ve který
    byl v jednotlivých letech hodnocen časopis 'IEEE Transactions on
    Industrial Electronics' (z_journal.name). Vypište také samotné
    hodnocení (z_year_field_journal.ranking).
*/

select ff.name, yfj.year, yfj.ranking
from z_journal j
join z_year_field_journal yfj on yfj.jid=j.jid
join z_field_ford ff on ff.fid=yfj.fid
where j.name='IEEE Transactions on Industrial Electronics';

/*
Výsledek: name year ranking

2.2 Electrical engineering, electronic engineering, information
engineering2017Decil

2.2 Electrical engineering, electronic engineering, information
engineering2018Decil

2.2 Electrical engineering, electronic engineering, information
engineering2019Decil

2.2 Electrical engineering, electronic engineering, information
engineering2020Decil

2.2 Electrical engineering, electronic engineering, information
engineering2021Decil

2.11 Other engineering and technologies2017Decil

2.11 Other engineering and technologies2018Decil

2.11 Other engineering and technologies2019Decil

2.11 Other engineering and technologies2020Decil

2.11 Other engineering and technologies2021Decil

Poznámka: Vidíme, že časopis může být v jednom roce hodnocen (Decil, Q1,
Q2, Q3, Q4) v různých oborech (stejně nebo různě).

*/

/*
 8. příklad: Vypište všechny obory FORD (z_field_ford.name), ve který
    byl v roce publikování hodnocen článek 'A Novel Method for Detection
    of Covered Conductor Faults in Medium Voltage Overhead Line Systems'
    (z_article.name). Vypište také samotné hodnocení
    (z_year_field_journal.ranking).
*/

select ff.name, yfj.ranking
from z_article a
join z_year_field_journal yfj on yfj.jid=a.jid and yfj.year=a.year
join z_field_ford ff on ff.fid=yfj.fid
where a.name='A Novel Method for Detection of Covered Conductor Faults in Medium Voltage Overhead Line Systems';
/*
Výsledek: name ranking

2.2 Electrical engineering, electronic engineering, information
engineeringDecil

2.11 Other engineering and technologiesDecil


Poznámka: z_article a z_year_field_journal spojujeme dle atributů year a
jid, jelikož časopisy jsou hodnoceny v oborech každý rok, tzn.
z_year_field_journal je hodnocení časopisů.

*/

/*
 9. příklad: Vypište počet článků instituce 'Vysoká škola báňská -
    Technická univerzita Ostrava' (z_institution.name) (hodnocených v
    roce publikování) v oboru FORD '1.2 Computer and Information
    Sciences' (z_field_ford.name).

*/
select count(a.aid) pocet_clanku
from z_institution i
join z_article_institution ai on i.iid=ai.iid
join z_article a on ai.aid=a.aid
join z_year_field_journal yfj on a.year=yfj.year and a.jid=yfj.jid
join z_field_ford ff on yfj.fid=ff.fid
where i.name='Vysoká škola báňská - Technická univerzita Ostrava'
and ff.name='1.2 Computer and Information Sciences';

/*
Výsledek: pocet_clanku

245


Poznámka: z_article a z_year_field_journal spojujeme dle atributů year a
jid, jelikož časopisy jsou hodnoceny v oborech každý rok.
*/

/*

10. příklad: Vypište počet článků instituce 'Vysoká škola báňská -
    Technická univerzita Ostrava' (z_institution.name) v jednotlivých
    hodnoceních (z_year_field_journal.ranking) všech oborů FORD.

*/
select yfj.ranking as ranking, count(distinct a.aid) pocet_clanku
from z_institution i
join z_article_institution ai on i.iid=ai.iid
join z_article a on ai.aid=a.aid
join z_year_field_journal yfj on a.year=yfj.year and a.jid=yfj.jid
where i.name='Vysoká škola báňská - Technická univerzita Ostrava'
group by yfj.ranking;

/*
Výsledek:

ranking pocet_clanku

Decil133

Q1402

Q2918

Q3640

Q4636


Poznámky:

 1.

    Časopis může být hodnocen ve více oborech FORD, duplicity stejného
    hodnocení v různých oborech jsme odstranili pomocí count(distinct a.aid)

 2.

    Kvůli různým hodnocením v různých oborech, by nás nemělo překvapit,
    že součet počtu článků v tomto dotazu je 2729, ale celkový počet
    článků instituce je 2363:
*/

select count(*)
from z_article a
         join z_article_institution ai on a.aid = ai.aid
         join z_institution i on ai.iid = i.iid
where i.name = 'Vysoká škola báňská - Technická univerzita Ostrava'



-- naleznete autory, kteri publikovali vice jak 600 clanku
-- vypis setridte podle poctu clanku

-- vysledek
/*
 +-------------+------------+
|name         |pocet_clanku|
+-------------+------------+
|Bi, R.       |609         |
|Choudhury, S.|611         |
|Banerjee, S. |649         |
|Buchanan, J. |657         |
|Cho, S.      |701         |
|Das, S.      |704         |
|Chang, P.    |705         |
|Bocci, A.    |872         |
|Chen, Y.     |891         |
|Chen, X.     |909         |
+-------------+------------+

 */

select name, count(*) as pocet_clanku
from z_author
join z_article_author on z_author.rid = z_article_author.rid
group by name
having count(*) > 600
order by pocet_clanku




-- Pro autora 'Choudhury, S.' vypiste
-- * Na kolika institucich publikoval
-- * Pocet casopisu, ve kterych publikoval v roce 2017

-- vysledek
/*
 +---------------+-------------------+
|pocet_instituci|pocet_casopisu_2017|
+---------------+-------------------+
|6              |9                  |
+---------------+-------------------+
 */

select (
    select count(distinct ai.iid)
    from z_article_institution ai
    join z_article_author aa on ai.aid = aa.aid
    where aa.rid = z_author.rid
   ) as pocet_instituci,
    (
        select count(distinct a.jid)
        from z_article a
        join z_article_author aa on a.aid = aa.aid
        where aa.rid = z_author.rid and a.year = 2017
        ) as pocet_casopisu_2017
from z_author
where name LIKE 'Choudhury, S.%'



select name
from z_field_ford
where name like '2.4%'

-- Naleznete autory, kteri nikdy nepublikovali v casopise 'scientific reports'
-- a zaroven publikovali v oboru FORD '2.4 Chemical engineering'

-- pocet zaznamu ve vysledku: 4193

select *
from z_author
where rid NOT IN (
    select rid
    from z_article_author aa
    join dbo.z_article za on aa.aid = za.aid
    join dbo.z_journal zj on za.jid = zj.jid
    where zj.name = 'scientific reports'
    )
and exists(
    select 1
    from z_article_author aa
    join dbo.z_article a on aa.aid = a.aid
    join dbo.z_year_field_journal zyfj on a.jid = zyfj.jid and a.year = zyfj.year
    join dbo.z_field_ford zff on zyfj.fid = zff.fid
    where zff.name LIKE '2.4%' AND z_author.rid = aa.rid
)



-- spočítejte statistiku, kolik autorů vydalo stejný počet článků
-- vystup  pocet_clanku | pocet_autoru
/* Pocet zaznamu ve vysledku: 518
+------------+------------+
|pocet_clanku|pocet_autoru|
+------------+------------+
|26          |245         |
|60          |28          |
|94          |13          |
|128         |18          |
|162         |6           |
|196         |11          |
|251         |6           |
|285         |1           |
|319         |5           |
|353         |5           |
|...         |....        |
|...         |....        |
+------------+------------+
*/


select pocet_clanku, count(*) as pocet_autoru
from (select rid, count(*) as pocet_clanku
      from z_article_author
      group by rid) t
group by pocet_clanku



select string_agg(concat_ws('-', year, ranking), ', ')
from z_year_field_journal
where jid = 35
group by year
order by year

-- Vypiste jmena, ID casopisu s nejvesim poctem ruznych hodnoceni
-- pocet techto hodnoceni dejte taky do vystupu

/* Vystup
   +-----+-------------------------------------------------------------+-----------------------+
|jid  |name                                                         |pocet_ruznych_hodnoceni|
+-----+-------------------------------------------------------------+-----------------------+
|4661 |acm journal on computing and cultural heritage               |5                      |
|5034 |historia mathematica                                         |5                      |
|3442 |history and anthropology                                     |5                      |
|12682|journal of cultural economy                                  |5                      |
|35   |archive for history of exact sciences                        |5                      |
|11587|nationalities papers-the journal of nationalism and ethnicity|5                      |
+-----+-------------------------------------------------------------+-----------------------+

 */

select j.jid, name, count(distinct ranking) as pocet_ruznych_hodnoceni
from z_journal j
join dbo.z_year_field_journal zyfj on j.jid = zyfj.jid
group by j.jid, name
having count(distinct ranking) >= ALL(
    select count(distinct ranking) as pocet_hodnoceni
    from z_year_field_journal
    group by jid
)



-- Vypiste vsechny autory s nadprůměrným počtem publikaci článků (z_article.type='Article')
-- Prumer zaokrouhlete na cele cislo, vysledek setridte podle poctu clanku a jména autoro

/** Vysledek
  Počet záznamů ve výsledku: 28 422
+------------------------+------------+
|name                    |pocet_clanku|
+------------------------+------------+
|Aaboudd, M.             |7           |
|Abad, Cilia             |7           |
|Abbastabar, Hedayat     |7           |
|Abdallah, M. S.         |7           |
|Abdel-Mohsen, A. M.     |7           |
|Abdel-Wahab, Mohamed    |7           |
|Abdou, Ahmed            |7           |
|Abedi-Ardekani, Behnoush|7           |
|Abu Hassan, M.          |7           |
|Adamec, Zdenek          |7           |
+------------------------+------------+

 */

select aut.name, count(*) as pocet_clanku
from z_author aut
left join z_article_author on aut.rid = z_article_author.rid
left join z_article a on z_article_author.aid = a.aid and a.type='Article'
group by aut.name
having count(*) > (
    select ROUND(avg(CAST(pocet AS FLOAT)), 0)
    from (
        select count(*) as pocet
        from z_article_author ai
        left join dbo.z_article za on ai.aid = za.aid and za.type='Article'
        group by ai.rid
         ) t

    )
order by pocet_clanku, name

