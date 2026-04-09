SELECT code, year, capacity, count(distinct student_login) as member_count
FROM course c
JOIN studentcourse sc  ON c.code = sc.course_code
GROUP BY code, year, capacity;


SET SERVEROUTPUT ON;







DECLARE 
    CURSOR my_cursor (p_lname student.lname%type) IS SELECT login FROM student WHERE lname = p_lname; -- Opraven název sloupce
    
    v_pom student.login%type;
    
BEGIN
    FOR f_moje IN my_cursor ('Novak')    
        LOOP            
            dbms_output.put_line(f_moje.login);
        END LOOP;
        
END;




DECLARE 
    CURSOR t_cursor IS SELECT login, fname, lname FROM teacher;
    CURSOR c_cursor IS SELECT code, name from course;
    
    
BEGIN
    FOR f_teacher in t_cursor
        LOOP            
            dbms_output.put_line(t_teacher.login);
            
        END LOOP;
        
END;




--vypsat pro vsechny ucitele login jmeno prijmeni a potom v druhem cyklu jako kurzy uci


DECLARE
    
    v_pom NUMBER(2,0);

BEGIN

    v_pom := 1;

    LOOP
    
        EXIT WHEN v_pom > 10;
        
        
        dbms_output.put_line (v_pom);
        v_pom := v_pom + 1;
    
    
    END LOOP;

END;




--1.1
CREATE OR REPLACE PROCEDURE PAddStudentToCourse ( 
    p_student_login studentcourse.student_login%type, 
    p_course_code studentcourse.course_code%type, 
    p_year studentcourse.year%type) AS
    
    v_capacity course.capacity%type;
    v_member_count course.capacity%type;
    
    my_exception EXCEPTION;
    
BEGIN

    SELECT capacity INTO v_capacity FROM course WHERE code = p_course_code;
    SELECT count(distinct student_login) INTO v_member_count FROM studentcourse WHERE course_code = p_course_code
    AND year = p_year;
    
    IF (v_capacity > v_member_count) THEN
        insert into studentCourse (student_login, course_code, year)
        values (p_student_login, p_course_code, p_year);
        
    ELSE 
    
        RAISE my_exception;
    
    END IF;
    
    
EXCEPTION
    WHEN my_exception THEN
    dbms_output.put_line('Kapacita naplněna');

END;
/
EXEC PAddStudentToCourse('smi324', '460-img-101', 2020);

--1.2 + 1.3
CREATE OR REPLACE TRIGGER TInsertStudentCourse
BEFORE INSERT ON StudentCourse FOR EACH ROW
DECLARE
    v_capacity INT;
    v_cnt      INT;

    my_exception_T EXCEPTION;
BEGIN
    SELECT capacity INTO v_capacity
    FROM Course
    WHERE code = :NEW.course_code;

    SELECT COUNT(*) INTO v_cnt
    FROM StudentCourse
    WHERE course_code = :NEW.course_code
      AND year = :NEW.year;

    IF v_cnt >= v_capacity THEN
        RAISE my_exception_T;
    END IF;

EXCEPTION
    WHEN my_exception_T THEN
    dbms_output.put_line('Kapacita kurzu byla překročena');
END;

--1.4 + 2.3
CREATE OR REPLACE FUNCTION FAddStudent4(p_fname Student.fname%TYPE, p_lname Student.lname%TYPE,
    p_email Student.email%TYPE, p_grade Student.grade%TYPE, p_dateOfBirth Student.date_of_birth%TYPE) RETURN VARCHAR AS
    
    v_login Student.login%TYPE;
    v_gradeCapacity INT;
    v_cnt INT;
BEGIN
    v_login := FGetNextLogin(p_lname);

    IF p_grade = 1 THEN
        v_gradeCapacity := 20;
    ELSIF p_grade = 2 THEN
        v_gradeCapacity := 15;
    ELSE
        v_gradeCapacity := 10;
    END IF;
    
    SELECT COUNT(*) INTO v_cnt
    FROM Student
    WHERE grade = p_grade;

    IF v_cnt >= v_gradeCapacity THEN
        RETURN 'full';
    END IF;

    INSERT INTO Student (login, fname, lname, email, grade, date_of_birth)
    VALUES (v_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);

    RETURN v_login;

EXCEPTION
    WHEN OTHERS THEN
        RETURN 'error';
END;

--1.5

CREATE OR REPLACE PROCEDURE PDeleteTeacher(
    p_login Teacher.login%TYPE
) AS
    v_new_teacher Teacher.login%TYPE;
    v_other_teachers_cnt INT;
    v_teacher_exists INT;
BEGIN
    SELECT COUNT(*) INTO v_teacher_exists
    FROM Teacher
    WHERE login = p_login;

    IF v_teacher_exists = 0 THEN
        dbms_output.put_line('Ucitel neexistuje.');
        return;
    END IF;

    SELECT COUNT(*) INTO v_other_teachers_cnt
    FROM Teacher
    WHERE login != p_login;

    IF v_other_teachers_cnt = 0 THEN
        dbms_output.put_line('Ucitel nelze smazat - neexistuje zadny jiny ucitel.');
        return;
    END IF;

    SELECT login INTO v_new_teacher
    FROM (
        SELECT t.login, COUNT(c.code) AS taught_cnt
        FROM Teacher t
        LEFT JOIN Course c ON c.teacher_login = t.login
        WHERE t.login != p_login
        GROUP BY t.login
        ORDER BY taught_cnt, t.login
    )
    WHERE ROWNUM = 1;

    UPDATE Course
    SET teacher_login = v_new_teacher
    WHERE teacher_login = p_login;

    DELETE FROM Teacher
    WHERE login = p_login;

    COMMIT;
    dbms_output.put_line('Ucitel ' || p_login || ' byl smazan. Kurzy prevzal: ' || v_new_teacher);

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        dbms_output.put_line('Nastala chyba, zmena byla vracena zpet.');
END;

--2.1
CREATE OR REPLACE FUNCTION FLoginExists(p_login Student.login%TYPE) RETURN BOOLEAN AS
    v_count INT;

BEGIN
    SELECT COUNT(*) into v_count
    from Student
    WHERE login = p_login;

    IF v_count >=1 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;

--2.2
CREATE OR REPLACE FUNCTION FGetNextLogin(p_lname VARCHAR) RETURN VARCHAR AS
    v_i INT;
    v_login VARCHAR(6);
BEGIN
    v_i := 1;

    v_login := LOWER(SUBSTR(p_lname, 1, 3)) || LPAD(v_i, 3, '0');

    WHILE FLoginExists(v_login) LOOP
        v_i := v_i + 1;
        v_login := LOWER(SUBSTR(p_lname, 1, 3)) || LPAD(v_i, 3, '0');
    END LOOP;

    RETURN v_login;
END;


-- 3.1
-- a) Explicitní kurzor (OPEN, FETCH, CLOSE)
DECLARE 
    CURSOR c_students IS SELECT fname, lname FROM student;
    v_fname student.fname%TYPE;
    v_lname student.lname%TYPE;
BEGIN
    OPEN c_students;
    LOOP
        FETCH c_students INTO v_fname, v_lname;
        EXIT WHEN c_students%NOTFOUND;
        dbms_output.put_line(v_fname || ' ' || v_lname);
    END LOOP;
    CLOSE c_students;
END;


-- b) Kurzor FOR LOOP
DECLARE 
    CURSOR c_students IS SELECT fname, lname FROM student;
BEGIN
    FOR r_student IN c_students LOOP            
        dbms_output.put_line(r_student.fname || ' ' || r_student.lname);
    END LOOP;
END;


-- 3.2
CREATE OR REPLACE PROCEDURE PAwardStudents(p_year INT, p_amount NUMBER) AS
    v_current_amount NUMBER;
    v_count INT;
    
    CURSOR c_top_students IS
        SELECT student_login, SUM(points) AS total_points
        FROM StudentCourse
        WHERE year = p_year
        GROUP BY student_login
        ORDER BY total_points DESC;
BEGIN
    v_current_amount := p_amount;
    v_count := 0;

    FOR r_student IN c_top_students LOOP
        EXIT WHEN v_count >= 5;

        UPDATE Student
        SET account = NVL(account, 0) + v_current_amount
        WHERE login = r_student.student_login;

        v_current_amount := v_current_amount / 2;
        v_count := v_count + 1;
    END LOOP;
    
    COMMIT;
    dbms_output.put_line('Stipendia udelena uspesne.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;

--


