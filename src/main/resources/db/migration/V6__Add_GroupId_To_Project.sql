alter table project
add column group_id varchar(255) check (group_id in ('IS', 'ISAS', 'CS'));