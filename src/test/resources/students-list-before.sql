insert into student (mark, email, group_id, name, password, is_active, role)
values (61, 'stud00002608@study.ru', 'IS', 'Artem', '$2y$12$91GnmuxcWMcS64Y.Ac7bTe81bBb9cAP4v7pAQaET4xB2zpdIJBpam', 'true', 'ADMIN');

insert into student (mark, email, group_id, name, password, is_active, role)
values (76, 'stud00002206@study.ru', 'CS', 'Kirill', '$2y$12$fQqXU9iqj.xFw7.dTZ5yFeHseNFraEWOtWbLCVTQ9drEB0AIOTMcm', 'true', 'USER');

insert into student (mark, email, group_id, name, password, is_active, role)
values (99, 'stud00001501@study.ru', 'ISAS', 'Sergey', '$2y$12$91GnmuxcWMcS64Y.Ac7bTe81bBb9cAP4v7pAQaET4xB2zpdIJBpam', 'true', 'USER');

alter sequence student_student_id_seq restart with 10;