alter table project_theme
add column is_available boolean;

alter table project
rename column rejected_at to expires_at;