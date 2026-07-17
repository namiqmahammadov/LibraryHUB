--liquibase formatted sql

--changeset namiqmahammadov:add-is-active-to-users-003


ALTER TABLE users
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;