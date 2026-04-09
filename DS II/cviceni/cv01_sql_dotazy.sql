//cv 2.1
--Vypište kódy a názvy kurzů, které vyučuje ’Jerry Jordon’.

select c.code, c.name
    from Teacher t, course c
    where t.login = c.teacher_login and t.fname = 'Jerry' and t.lname = 'Jordon';
    
    -- 460-img-101	Image Processing
    -- 460-cns-922	Computer Networks
    -- 460-ds1-011	Database Systems I


//cv2.2
-- Vypište studenty, které v roce 2020 nebo 2021 učil ’Jerry Jordon’.

select distinct s.fname, s.lname, s.login
    from teacher t
    join course c on c.teacher_login = t.login
    join studentCourse sc on sc.course_code = c.code
    join student s on sc.student_login = s.login
    where t.fname = 'Jerry' and t.lname = 'Jordon' and sc.year in (2020, 2021);
    
    -- Robert	Baughman	bau764
    -- Christopher	Greco	gre227
    -- Thomas	Grigsby	gri453
    -- Michael	Silverman	sil012
    -- David	Royal	rol087
    -- Richard	McCrary	mcc676
    -- Joseph	Joy	joy087
    -- Daniel	Cabral	cab466
    -- Paul	Trout	tro552
    -- Mark	Rinehard	rin766
    
//cv2.2 alt
--
select login, fname, lname from student
where login in (
select distinct student_login from studentcourse
    where course_code in 
    (select code from course where teacher_login in
        (select login from teacher where fname = 'Jerry' and lname = 'Jordon')));
        
    -- bau764	Robert	Baughman
    -- cab466	Daniel	Cabral
    -- gre227	Christopher	Greco
    -- gri453	Thomas	Grigsby
    -- joy087	Joseph	Joy
    -- mcc676	Richard	McCrary
    -- rin766	Mark	Rinehard
    -- rol087	David	Royal
    -- sil012	Michael	Silverman
    -- tro552	Paul	Trout
        
//cv2.3  
-- Pro všechny studenty v doméně ’@vsb.cz’ vypište počet zapsaných kurzů.

select s.login, count(sc.course_code) as pocet_kurzu
    from student s
    left join studentCourse sc on sc.student_login = s.login
    where s.email like '%@vsb.cz'
    group by s.login;
    
    -- bau764	3
    -- cab466	7
    -- gri453	7
    -- joh357	2
    -- pen048	0
    -- rol087	1
    -- tro552	8

//cv2.4
-- Pro všechny studenty vypište, kolik je v roce 2020 učilo různých učitelů.

select s.fname, s.lname, count(distinct c.teacher_login) as pocet
    from student s
    left join studentcourse sc on s.login = sc.student_login and sc.year = 2020
    left join course c on sc.course_code = c.code
    group by s.login, s.fname, s.lname;
    
    -- Robert	Baughman	1
    -- William	Satterfield	0
    -- Michael	Silverman	1
    -- Richard	McCrary	1
    -- Christopher	Greco	1
    -- Thomas	Grigsby	1
    -- Elizabeth	Brown	0
    -- Mark	Rinehard	1
    -- James	Gannon	1
    -- Erika	Pena	0
    -- Patricia	Johnson	0
    -- Charles	Kowalski	1
    -- Daniel	Cabral	2
    -- John	Farnsworth	1
    -- Paul	Trout	2
    -- Barbara	Jones	0
    -- Joseph	Joy	0
    -- Linda	Williams	0
    -- Mary	Smith	0
    -- David	Royal	1
    
//cv2.5
-- Pro každého učitele vypište kurz(y) s nejvyšší kapacitou, kapacita bude součástí výsledku. Učitele bez kurzů nevypisujte.

with max_kapacity as (
    select teacher_login, max(capacity) as max_cap
    from course
    group by teacher_login
)
select t.fname, t.lname, c.name, c.capacity
    from teacher t
    join course c on t.login = c.teacher_login
    join max_kapacity mk on t.login = mk.teacher_login and c.capacity = mk.max_cap;
    
    -- Peter	Burton	Database Systems II	5
    -- Jerry	Jordon	Computer Networks	10
    -- Walter	Perryman	Web Technologies	15
    -- Patrick	Newsom	Algebra	15
    -- Patrick	Newsom	Discrete Math	15
    -- Carl	Artis	Theory of Economy	15
    
//cv2.6
-- Vypište učitele, kteří vyučují největší počet kurzů.

with teach as
(
    select t.fname, t.lname, count(c.code) as pocet
    from Teacher t
    left join Course c ON t.login = c.teacher_login
    group by t.login, t.fname, t.lname
)
select * 
from teach
order by pocet desc 
fetch first 1 rows only;

    -- Carl	Artis	5

//cv2.7
-- Vypište studenty, které v roce 2020 nevyučoval ’Jerry Jordon’, ale zároveň tito studenti měli v tomto roce zapsán alespoň jeden kurz.

with jerrystudenti as(
select distinct s.login
    from student s 
    join studentcourse sc on sc.student_login = s.login
    join course c on c.code = sc.course_code
    join teacher t on t.login = c.teacher_login
    where t.fname = 'Jerry' and t.lname = 'Jordon' and sc.year = 2020)
select distinct s.fname, s.lname
    from student s
    join studentcourse sc on s.login = sc.student_login
    where sc.year = 2020 and s.login not in (select login from jerrystudenti)
    
    
    -- Richard	McCrary
    -- Charles	Kowalski
    -- James	Gannon
    -- Mark	Rinehard
    -- John	Farnsworth

//cv2.8
-- Pro každý kurz vypište průměrný počet studentů, kteří měli daný kurz zapsán v jednotlivých letech. Uvažujte jen kurzy, na které byl či je zapsán alespoň jeden student.

with pocty_roky as (
    select c.code, c.name, sc.year, count(sc.student_login) as pocet_v_roce
    from course c
    join studentcourse sc on c.code = sc.course_code
    group by c.code, c.name, sc.year
)
select code, name, avg(pocet_v_roce) as prumerny_pocet_studentu
from pocty_roky
group by code, name;

    -- 460-img-101	Image Processing	3,5
    -- 470-dis-902	Discrete Math	9
    -- 460-cns-922	Computer Networks	7
    -- 460-ds1-011	Database Systems I	3
    -- 470-alg-220	Algebra	7
    -- 460-jav-226	Java Technologies	2,5
    -- 460-ds2-012	Database Systems II	2
    -- 460-web-654	Web Technologies	6


//3.1
--Přidejte do tabulky Teacher učitele ’Peter Burton’ s loginem ’bur154’ a katedrou ’Department of Mathematic’.

insert into Teacher(fname, lname, login, department)
    values ('Peter', 'Burton', 'bur154', 'Department of Mathematic');
    
//3.2
-- Nastavte kurzu ’Database Systems II’ nově vloženého učitele ’bur154’.

update course
set teacher_login = 'bur154'
where course.name = 'Database Systems II';

//3.3
-- Zrušte jedním SQL příkazem zápis kurzů (tj., záznamy v StudentCourse), které vyučuje ’Carl Artis’.

delete from studentcourse
where course_code in (
    select c.code 
    from course c
    join teacher t on c.teacher_login = t.login
    where t.fname = 'Carl' and t.lname = 'Artis'
);

//3.4
-- Nastavte studentovi ’Barbara Jones’ účast pro rok 2021 na všech kurzech, které vyučuje ’Walter Perryman’.

update studentcourse
set year = 2021
where student_login = (select login from student where fname = 'Barbara' and lname = 'Jones')
  and course_code in (
    select c.code 
    from course c
    join teacher t on c.teacher_login = t.login
    where t.fname = 'Walter' and t.lname = 'Perryman'
);

//4.1
-- Nastavte studentovi ’smi324’ datum narození na 2.3.1997.

update Student 
set date_of_birth = to_date('2.3.1997', 'dd.mm.yyyy')
where login = 'smi324';

//4.2
-- Pro každého studenta vypište jeho věk.

select fname, lname, floor((sysdate - date_of_birth) / 365.25) as vek
from student;

    -- Mary	Smith	28
    -- Patricia	Johnson	28
    -- Linda	Williams	28
    -- Barbara	Jones	28
    -- Elizabeth	Brown	27
    -- James	Gannon	27
    -- John	Farnsworth	27
    -- Robert	Baughman	29
    -- Michael	Silverman	28
    -- William	Satterfield	29
    -- David	Royal	29
    -- Richard	McCrary	29
    -- Charles	Kowalski	30
    -- Joseph	Joy	31
    -- Thomas	Grigsby	31
    -- Christopher	Greco	31
    -- Daniel	Cabral	31
    -- Paul	Trout	32
    -- Mark	Rinehard	33
    -- Erika	Pena	34

//4.3
-- Ke každému studentovi vypište datum, kdy oslavil své 18-té narozeniny.

select fname, lname, add_months(date_of_birth, 18 * 12) as osmnacte_narozeniny
from student;

    -- Mary	Smith	02.03.15
    -- Patricia	Johnson	23.09.15
    -- Linda	Williams	02.02.16
    -- Barbara	Jones	12.12.15
    -- Elizabeth	Brown	06.06.16
    -- James	Gannon	22.04.16
    -- John	Farnsworth	02.04.16
    -- Robert	Baughman	25.02.15
    -- Michael	Silverman	15.03.15
    -- William	Satterfield	11.11.14
    -- David	Royal	12.10.14
    -- Richard	McCrary	20.12.14
    -- Charles	Kowalski	12.05.13
    -- Joseph	Joy	04.01.13
    -- Thomas	Grigsby	27.09.12
    -- Christopher	Greco	07.11.12
    -- Daniel	Cabral	05.05.12
    -- Paul	Trout	29.11.11
    -- Mark	Rinehard	10.04.10
    -- Erika	Pena	22.02.10

//4.4
-- Vypište ke každému studentovi v samostatných sloupcích rok, měsíc a den jeho narození.

select fname, lname, 
       extract(year from date_of_birth) as rok,
       extract(month from date_of_birth) as mesic,
       extract(day from date_of_birth) as den
from student;

    -- Mary	Smith	1997	3	2
    -- Patricia	Johnson	1997	9	23
    -- Linda	Williams	1998	2	2
    -- Barbara	Jones	1997	12	12
    -- Elizabeth	Brown	1998	6	6
    -- James	Gannon	1998	4	22
    -- John	Farnsworth	1998	4	2
    -- Robert	Baughman	1997	2	25
    -- Michael	Silverman	1997	3	15
    -- William	Satterfield	1996	11	11
    -- David	Royal	1996	10	12
    -- Richard	McCrary	1996	12	20
    -- Charles	Kowalski	1995	5	12
    -- Joseph	Joy	1995	1	4
    -- Thomas	Grigsby	1994	9	27
    -- Christopher	Greco	1994	11	7
    -- Daniel	Cabral	1994	5	5
    -- Paul	Trout	1993	11	29

//4.5
--  Vypište studenty, kteří se narodili ve stejné datum jako student ’Michael Silverman’ s tolerancí 3 měsíce.

select s.fname, s.lname
from student s
where s.date_of_birth between 
    (select add_months(date_of_birth, -3) from student where fname = 'Michael' and lname = 'Silverman')
    and 
    (select add_months(date_of_birth, 3) from student where fname = 'Michael' and lname = 'Silverman')
and s.login != (select login from student where fname = 'Michael' and lname = 'Silverman');

    -- Mary	Smith
    -- Robert	Baughman
    -- Richard	McCrary

//5.1
-- Vypište celá jména všech učitelů převedená na velká písmena. Celé jméno bude tvořeno jménem, mezerou a příjmením.

select upper(fname || ' ' || lname) as cele_jmeno
from teacher;

    -- JERRY JORDON
    -- DENNIS GILMAN
    -- WALTER PERRYMAN
    -- PATRICK NEWSOM
    -- PETER MENARD
    -- HAROLD MARTINO
    -- DOUGLAS GRAF
    -- CARL ARTIS
    -- PETER BURTON

//5.2
-- Pro každého učitele vypište délku (počet znaků) jeho příjmení.

select lname, length(lname) as delka_prijmeni
from teacher;

    -- Jordon	6
    -- Gilman	6
    -- Perryman	8
    -- Newsom	6
    -- Menard	6
    -- Martino	7
    -- Graf	4
    -- Artis	5
    -- Burton	6

//5.3
-- Vypište názvy všech kurzů bez mezer, tj. např. ’Discrete Math’ bude vypsáno jako ’DiscreteMath’.

select replace(name, ' ', '') as nazev_bez_mezer
from course;

    -- DatabaseSystemsI
    -- DatabaseSystemsII
    -- ComputerNetworks
    -- ImageProcessing
    -- JavaTechnologies
    -- Python
    -- WebTechnologies
    -- Algebra
    -- DiscreteMath
    -- Microeconomics
    -- Macroeconomics
    -- Management
    -- TheoryofEconomy
    -- Advertisement

//5.4
-- Pro všechny kurzy vypište jejich kapacitu zarovnanou na 3 číslice, tj. např. kapacita 5 bude vypsána jako ’005’.

select name, lpad(capacity, 3, '0') as kapacita_format
from course;

    -- Database Systems I	005
    -- Database Systems II	005
    -- Computer Networks	010
    -- Image Processing	005
    -- Java Technologies	008
    -- Python	010
    -- Web Technologies	015
    -- Algebra	015
    -- Discrete Math	015
    -- Microeconomics	008
    -- Macroeconomics	008
    -- Management	008
    -- Theory of Economy	015
    -- Advertisement	008

//5.5
-- Vypište podřetězce kódů kurzů mezi 5-tým až 7-mým znakem, tj. mezi pomlčkami.

select code, substr(code, 5, 3) as cast_kodu
from course;

    -- 420-adv-061	adv
    -- 420-mac-022	mac
    -- 420-man-111	man
    -- 420-mic-061	mic
    -- 420-toe-166	toe
    -- 460-cns-922	cns
    -- 460-ds1-011	ds1
    -- 460-ds2-012	ds2
    -- 460-img-101	img
    -- 460-jav-226	jav
    -- 460-pyt-001	pyt
    -- 460-web-654	web
    -- 470-alg-220	alg
    -- 470-dis-902	dis

//5.6
-- Vypište názvy kurzů s vynecháním prvních dvou písmen.

select substr(name, 3) as orezany_nazev
from course;

    -- tabase Systems I
    -- tabase Systems II
    -- mputer Networks
    -- age Processing
    -- va Technologies
    -- thon
    -- b Technologies
    -- gebra
    -- screte Math
    -- croeconomics
    -- croeconomics
    -- nagement
    -- eory of Economy
    -- vertisement

//5.7
-- Pro všechny kurzy vypište pozici první mezery v jejich názvu.

select name, instr(name, ' ') as pozice_mezery
from course;
    
    -- Database Systems I	9
    -- Database Systems II	9
    -- Computer Networks	9
    -- Image Processing	6
    -- Java Technologies	5
    -- Python	0
    -- Web Technologies	4
    -- Algebra	0
    -- Discrete Math	9
    -- Microeconomics	0
    -- Macroeconomics	0
    -- Management	0
    -- Theory of Economy	7
    -- Advertisement	0