CREATE TABLE transactions
(
    id   SERIAL8 PRIMARY KEY,
    type INT       NOT NULL,

    time TIMESTAMP NOT NULL
);

CREATE TABLE transactions_cash
(
    id         BIGINT PRIMARY KEY,
    account_id BIGINT         NOT NULL,
    amount     NUMERIC(11, 2) NOT NULL,
    time       TIMESTAMP      NOT NULL
);

CREATE TABLE transactions_account
(
    id            BIGINT PRIMARY KEY,
    account_from  BIGINT         NOT NULL,
    account_to    BIGINT         NOT NULL,
    description   VARCHAR(255)   NOT NULL,
    amount        NUMERIC(11, 2) NOT NULL,
    currency_from VARCHAR(3)     NOT NULL,
    currency_to   VARCHAR(3)     NOT NULL,
    exchange_rate NUMERIC(6, 2)  NOT NULL,
    time          TIMESTAMP      NOT NULL,

    FOREIGN KEY (id) REFERENCES transactions (id) ON DELETE RESTRICT,
    FOREIGN KEY (account_from) REFERENCES account (id) ON DELETE RESTRICT,
    FOREIGN KEY (account_to) REFERENCES account (id) ON DELETE RESTRICT
);
