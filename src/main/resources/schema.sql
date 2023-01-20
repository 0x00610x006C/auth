create table if not exists "users"(
    id identity not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    EMAIL varchar(255) not null,
    password varchar(255) not null
);
