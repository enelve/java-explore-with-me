CREATE TABLE IF NOT EXISTS hits (
                                    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    app        VARCHAR(255),
                                    uri        VARCHAR(255),
                                    ip         VARCHAR(15),
                                    timestamp  TIMESTAMP
);