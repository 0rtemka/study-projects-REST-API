alter table student
    add column password  varchar(1024),
    add column role      varchar(64) check ( role in ('USER', 'ADMIN')),
    add column is_active boolean