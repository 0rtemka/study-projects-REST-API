insert into project_theme (topic, added_at, group_id)
values ('Concurrency in Java', timestamp '2023-07-01 12:00:00', 'IS');

insert into project_theme (topic, added_at, group_id)
values ('Concurrency in Python', timestamp '2023-06-16 12:00:00', 'IS');

insert into project_theme (topic, added_at, group_id)
values ('Concurrency in Kotlin', timestamp '2023-07-01 12:00:00', 'IS');

insert into project_theme (topic, added_at, group_id)
values ('Stream API in Java', timestamp '2023-06-01 12:00:00', 'ISAS');

insert into project_theme (topic, added_at, group_id)
values ('LINQ in C#', timestamp '2023-06-01 12:00:00', 'ISAS');

insert into project_theme (topic, added_at, group_id)
values ('Collections framework in Java', timestamp '2023-07-11 12:00:00', 'CS');

alter sequence project_theme_theme_id_seq restart with 10;

insert into student (mark, email, group_id, name, password, is_active, role)
values (61, 'stud00002608@study.ru', 'IS', 'Artem', '$2y$12$91GnmuxcWMcS64Y.Ac7bTe81bBb9cAP4v7pAQaET4xB2zpdIJBpam', 'true', 'ADMIN');
