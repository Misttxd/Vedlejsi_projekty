SET SERVEROUTPUT ON;

BEGIN
dbms_output.put_line('Hello World!');
END;



DECLARE

    v_login CHAR(6);
    v_fname VARCHAR2(30);
    v_lname VARCHAR2(50);
    v_email VARCHAR2(50);
    v_grade NUMBER(38,0);
    v_date DATE;
    
    v_record student%rowtype;

BEGIN

    v_login := 'dev66';
    v_fname := 'Frantisek';
    v_lname := 'Parek';
    v_email := 'parek@seznam.cz';
    v_grade := 1;
    v_date := TO_DATE('1992/05/06', 'yyyy/mm/dd');
        
    INSERT INTO Student (login, fname, lname, email, grade, date_of_birth)
    VALUES (v_login, v_fname, v_lname, v_email, v_grade, v_date);


    dbms_output.put_line('Student vlozen.');
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('Student nebyl vlozen.');
END;


DECLARE 
    v_zaznam student%rowtype;
    
BEGIN 

    SELECT login, fname, lname INTO v_zaznam.login, v_zaznam.fname, v_zaznam.lname 
    FROM student 
    WHERE login = 'abc123';
    
    dbms_output.put_line('Login: ' || v_zaznam.login || ' Jmeno: ' || v_zaznam.fname || ' Prijmeni: ' || v_zaznam.lname);
    
EXCEPTION

    WHEN TOO_MANY_ROWS THEN 
    dbms_output.put_line('az moc radku');
    
    WHEN NO_DATA_FOUND THEN
    dbms_output.put_line ('neni zadne data');

END;

-- DÚ2:

DECLARE

    v_login1 CHAR(6);
    v_login2 CHAR(6);

BEGIN

    v_login1 := 'par123';
    v_login2 := 'per321';

    INSERT INTO TEACHER(department, fname, lname, login)
    VALUES ('temp_dep', 'temp_fname', 'temp_lname', 'tmp676');

    UPDATE COURSE
    SET teacher_login = 'tmp676'
    WHERE teacher_login = v_login1;

    UPDATE COURSE
    SET teacher_login = v_login1
    WHERE teacher_login = v_login2;

    UPDATE COURSE
    SET teacher_login = v_login2
    WHERE teacher_login = 'tmp676';

    DELETE FROM Teacher
    WHERE login = 'tmp676';

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;

END;


