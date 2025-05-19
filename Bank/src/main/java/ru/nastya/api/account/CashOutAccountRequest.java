package ru.nastya.api.account;

import lombok.Data;

@Data
public class CashOutAccountRequest {
    private long accountId;
    private long amount;
}