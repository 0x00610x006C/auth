create table if not exists "users"(
    id identity not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null
);

alter table if exists "users"
    add constraint if not exists uq_email unique (EMAIL);