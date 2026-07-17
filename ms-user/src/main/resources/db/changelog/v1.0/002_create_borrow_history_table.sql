--liquibase formatted sql

--changeset namiqmahammadov:create-borrow-history-table-002


CREATE TABLE borrow_history
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    book_id    BIGINT       NOT NULL,
    book_title VARCHAR(255) NOT NULL,
    borrow_at  TIMESTAMP    NOT NULL,
    return_at  TIMESTAMP,
    status     VARCHAR(20)  NOT NULL
);