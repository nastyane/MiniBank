package ru.nastya.api.savingAccount;

import lombok.Data;

@Data
public class CashInSavingAccountRequest {
    private long accountId;
    private long amount;

}

