create table if not exists public.authors
(
    id serial primary key,
    first_name   varchar(16) not null,
    last_name  varchar(128)
    );