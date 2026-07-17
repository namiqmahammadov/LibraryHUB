--liquibase formatted sql

--changeset namiqmahammadov:create-users-table-001

CREATE TABLE users
(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    full_name       VARCHAR(100),
    role            VARCHAR(20)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP,
    last_login_time TIMESTAMP
);