delete from student_project;

delete from project_theme;

delete from student;

delete from project;

alter sequence project_project_id_seq restart with 1;

alter sequence project_theme_theme_id_seq restart with 1;

alter sequence student_student_id_seq restart with 1;
