alter table project_db.public.project_theme
add column added_at timestamp(6);

alter table project_db.public.project
add column rejected_at timestamp(6);