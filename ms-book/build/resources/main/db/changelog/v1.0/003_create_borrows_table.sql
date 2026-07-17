--liquibase formatted sql

--changeset namiqmahammadov:create-borrows-table-001

CREATE TABLE borrows
(
    id        BIGSERIAL PRIMARY KEY,
    book_id   INT         NOT NULL,
    user_id   INT         NOT NULL,
    borrow_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    due_date  DATE        NOT NULL,
    return_at TIMESTAMP,
    status    VARCHAR(20) NOT NULL
);