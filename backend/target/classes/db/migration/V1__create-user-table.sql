CREATE TABLE users
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username       VARCHAR(20) UNIQUE  NOT NULL,
    email          VARCHAR(254) UNIQUE NOT NULL,
    password       VARCHAR,
    google_subject VARCHAR UNIQUE,
    role           VARCHAR             NOT NULL,
    experience     INT                 NOT NULL
);