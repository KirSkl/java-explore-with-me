DROP TABLE IF EXISTS hits; --закомментировать для корректной работы приложения

CREATE TABLE IF NOT EXISTS hits
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app        VARCHAR(64)                             NOT NULL,
    uri        VARCHAR(512)                            NOT NULL,
    ip         VARCHAR(16),
    time_stamp TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);