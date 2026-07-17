--liquibase formatted sql

--changeset namiqmahammadov:create-books-table-001

CREATE TABLE books
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(150) NOT NULL,
    isbn             VARCHAR(20)  NOT NULL UNIQUE,
    description      TEXT,
    category_id      BIGINT       NOT NULL,
    total_copies     INT          NOT NULL,
    available_copies INT          NOT NULL,
    published_year   INT,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    is_active        BOOLEAN      NOT NULL
);