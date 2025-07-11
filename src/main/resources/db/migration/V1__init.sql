CREATE TABLE "USERS"
(
    USER_ID    uuid primary key not null,
    USERNAME   varchar(255)  unique   not null,
    PASSWORD   varchar(255)     null,
    CREATED_AT timestamp        not null
);