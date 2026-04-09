//1.1
create or replace procedure PPrint(p_text varchar2) as
begin
    dbms_output.put_line(p_text);
end;

//1.2
create or replace procedure PAddStudent1(p_login Student.login%type, p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) as
begin
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (p_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);
end;

// 1.3
create or replace procedure PAddStudent2(p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) as
    
    v_login Student.login%type;
begin
    v_login := lower(substr(p_lname, 1, 3)) || '000';
    
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (v_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);
end;

// 1.4
create or replace procedure PAddStudent3(p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) as
    
    v_studentCount int;
    v_login Student.login%type;
begin
    select count(*) + 1 into v_studentCount
    from Student;

    v_login := lower(substr(p_lname, 1, 3)) || lpad(v_studentCount, 3, '0');
    
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (v_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);
end;

//2.1
create or replace function FAddStudent1 (p_login Student.login%type, p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) return varchar as
begin
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (p_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);
    
    return 'OK';
    
exception
    when others then 
        return 'error';
end;

//2.2
create or replace function FAddStudent3(p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) return varchar as
    
    v_studentCount int;
    v_login Student.login%type;
begin
    select count(*) + 1 into v_studentCount
    from Student;

    v_login := lower(substr(p_lname, 1, 3)) || lpad(v_studentCount, 3, '0');
    
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (v_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);
    
    return v_login;
    
exception
    when others then
        return 'error';
end;

// 2.3
create or replace function FGetLogin(p_lname Student.lname%type) return Student.login%type as
    v_studentCount int;
begin   
    select count(*) + 1 into v_studentCount
    from Student;

    return lower(substr(p_lname, 1, 3)) || lpad(v_studentCount, 3, '0');
end;

// 2.4
create or replace function FAddStudent3(p_fname Student.fname%type, p_lname Student.lname%type,
    p_email Student.email%type, p_grade Student.grade%type, p_dateOfBirth Student.date_of_birth%type) return varchar as
    
    v_login Student.login%type;
begin
    v_login := FGetLogin(p_lname);
    
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (v_login, p_fname, p_lname, p_email, p_grade, p_dateOfBirth);

    return v_login;
    
exception
    when others then
        return 'error';
end;

//3.1
create or replace trigger TInsertStudent after insert on Student for each row
begin 
    PPrint(: new.login || ': ' || :new.fname || ' ' || :new.lname);
end;

//3.2
create or replace trigger TDeleteStudent before delete on Student for each row
begin
    PPrint(:old.login || ': ' || :old.fname || ' ' || :old.lname);
end;

// 3.3
create or replace trigger TUpdateStudent after update on Student for each row
begin
    PPrint('Pred zmenou: ' || :old.fname || ' ' || :old.lname || ', rocnik ' || :old.grade);
    PPrint('Po zmene:    ' || :new.fname || ' ' || :new.lname || ', rocnik ' || :new.grade);
end;



// 3.4
create or replace trigger TInsertStudent1 after insert on Student for each row
declare
    v_login Student.login%type;
    v_year int;
begin
    v_login := :new.login;
    v_year := extract(year from current_timestamp);

    insert into StudentCourse (student_login, course_code, year)
    select v_login, code, v_year
    from Course
    where grade = 1;
end;


// 3.5
create or replace trigger TInsertStudent2 before insert on Student for each row
begin
    :new.login := FGetLogin(:new.lname);
end;

insert into Student (fname, lname, email, grade, date_of_birth)
values ('Jan', 'Novak', 'jan.novak@vsb.cz', 1, to_date('2000-01-01', 'yyyy-mm-dd'));


// 4.1
create or replace procedure PStudentBecomeTeacher(p_login Student.login%type,
    p_department Teacher.department%type) as

begin
    insert into Teacher (login, fname, lname, department)
    select login, fname, lname, p_department
    from Student
    where login = p_login;
    
    delete from Student
    where login = p_login;

    commit;
exception
    when others then
        rollback;
end;

// 4.2
create or replace procedure PStudentAssignment(p_fname Student.fname%type, p_lname Student.lname%type,
    p_dateOfBirth date) as
    
    v_login Student.login%type;
    v_email Student.email%type;
    v_year StudentCourse.year%type;
begin
    v_login := FGetLogin(p_lname);
    v_email := v_login || '@vsb.cz';
    v_year := extract(year from current_timestamp);
    
    insert into Student (login, fname, lname, email, grade, date_of_birth)
    values (v_login, p_fname, p_lname, v_email, 1, p_dateOfBirth);
    
    insert into StudentCourse (student_login, course_code, year)
    select v_login, code, v_year
    from Course
    where grade = 1;

    commit;
exception
    when others then
        rollback;    
end;

//DÚ1
create or replace procedure PSendEmail (p_email varchar2, p_subject varchar2, p_body integer) as
begin

    PPrint ('Komu: ' || p_email);
    PPrint ('Předmět: ' || p_subject);
    PPrint (p_body);
    
end;

create or replace trigger TSendEMail after insert on StudentCourse for each row
declare
    v_student_fname Student.fname%type;
    v_student_lname Student.lname%type;
    v_student_email Student.email%type;
    v_teacher_fname Teacher.fname%type;
    v_teacher_lname Teacher.lname%type;
    v_course_name Course.name%type;
    v_subject varchar2(100);
    v_body varchar2(500);
begin
    select fname, lname, email into v_student_fname, v_student_lname, v_student_email
    from Student
    where login = :new.student_login;
    
    select Course.name, Teacher.fname, Teacher.lname into v_course_name, v_teacher_fname, v_teacher_lname
    from Teacher join Course on Teacher.login = Course.teacher_login
    where Course.code = :new.course_code;
    
    v_subject := 'Prihlaseni ke kurzu ' || v_course_name;
    
    v_body := 'Vazeny studente ' || v_student_fname || ' ' || v_student_lname || ', dne ' || to_char(current_timestamp, 'dd.mm.yyyy hh24:mi:ss') ||
        ' jste byl prihlasen do kurzu ' || v_course_name || '. Vyucujicim kurzu je ' || v_teacher_fname ||
        ' ' || v_teacher_lname || '.';
        
    PSendEMail(v_student_email, v_subject, v_body);
end;

//DÚ 2
create or replace function FGetStudentScore(p_login Student.login%type) return number as
    v_ptsReceived int;
    v_ptsTotal int;
begin
    select coalesce(sum(points), 0), count(*) * 100 into v_ptsReceived, v_ptsTotal
    from StudentCourse
    where student_login = p_login;
    
    return v_ptsReceived / v_ptsTotal;
end;


create or replace procedure PCheckStudents(p_amount number) as
    v_avgPts number;
begin
    select avg(sum_points) into v_avgPts
    from
    (
        select sum(points) as sum_points
        from StudentCourse
        group by student_login
    ) T;
    
    update Student
    set account_balance = account_balance + p_amount
    where
    (
        select sum(points)
        from StudentCourse
        where StudentCourse.student_login = Student.login
    ) > v_avgPts;

    update Student
    set account_balance = account_balance - p_amount
    where
    (
        select sum(points)
        from StudentCourse
        where StudentCourse.student_login = Student.login
    ) < v_avgPts;
    
    delete from Student
    where account_balance < 0;
    
    commit;
    
exception
    when others then
        rollback;
end;