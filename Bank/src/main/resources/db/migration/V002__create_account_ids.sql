CREATE TABLE account
(
    id   SERIAL8 PRIMARY KEY,
    type INT NOT NULL
);

CREATE TABLE account_cash
(
    id       BIGINT PRIMARY KEY,
    user_id  BIGINT         NOT NULL,
    amount   NUMERIC(11, 2) NOT NULL,
    currency VARCHAR(3),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    FOREIGN KEY (id) REFERENCES account (id) ON DELETE RESTRICT
);

CREATE TABLE account_saving
(
    id       BIGINT PRIMARY KEY,
    user_id  BIGINT         NOT NULL,
    amount   NUMERIC(11, 2) NOT NULL,
    currency VARCHAR(3)     NOT NULL,
    percent  NUMERIC(4, 2)  NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    FOREIGN KEY (id) REFERENCES account (id) ON DELETE RESTRICT
);