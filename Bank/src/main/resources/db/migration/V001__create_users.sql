CREATE TABLE users
(
    id         SERIAL8 PRIMARY KEY,
    last_name  VARCHAR(255)       NOT NULL,
    first_name VARCHAR(255)       NOT NULL,
    patronymic VARCHAR(255)       NOT NULL,
    login      VARCHAR(30) UNIQUE NOT NULL,
    password   VARCHAR(255)       NOT NULL,
    phone      VARCHAR(15)        NOT NULL
);
