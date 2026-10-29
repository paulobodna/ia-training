CREATE TABLE attempts
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seed              BIGINT      NOT NULL,
    generator_version SMALLINT    NOT NULL,
    difficulty        SMALLINT    NOT NULL,
    tips_used         SMALLINT    NOT NULL,
    completed_at      TIMESTAMPTZ NOT NULL,
    time_elapsed      interval    NOT NULL,
    xp_obtained       SMALLINT    NOT NULL,
    user_id           BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);