package ru.nastya.api.savingAccount;

import lombok.Data;

@Data
public class CashOutSavingAccountRequest {
    private long accountId;
    private long amount;

}