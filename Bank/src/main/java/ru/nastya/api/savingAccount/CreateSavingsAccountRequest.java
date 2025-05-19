package ru.nastya.api.savingAccount;

import lombok.Data;

@Data
public class CreateSavingsAccountRequest {
    private long userId;
    private String currency;
}