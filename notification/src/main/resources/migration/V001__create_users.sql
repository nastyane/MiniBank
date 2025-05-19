CREATE TABLE notifications
(
    id          BIGSERIAL PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    amount     NUMERIC(11, 2),
    message    TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

