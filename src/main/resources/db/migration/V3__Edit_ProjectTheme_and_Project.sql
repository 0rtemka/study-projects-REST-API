alter table project_theme
add column added_at timestamp(6);

alter table project
add column rejected_at timestamp(6);