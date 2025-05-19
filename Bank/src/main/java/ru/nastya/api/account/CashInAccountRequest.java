package ru.nastya.api.account;


import lombok.Data;

@Data
public class CashInAccountRequest {
    private long accountId;
    private long amount;
}
