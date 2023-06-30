create table project
(
    project_id serial not null,
    mark       integer,
    taken_at   timestamp(6),
    topic      varchar(255),
    primary key (project_id)
);

create table project_theme
(
    theme_id serial not null,
    group_id varchar(255) check (group_id in ('IS', 'ISAS', 'CS')),
    topic    varchar(255),
    primary key (theme_id)
);

create table student
(
    student_id serial not null,
    mark       integer,
    email      varchar(255),
    name       varchar(255),
    primary key (student_id)
);

create table student_project
(
    project_id integer not null,
    student_id integer not null,
    primary key (project_id, student_id)
)
