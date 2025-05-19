package ru.nastya.api.account;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private int userId;
    private double balance;
    private String currency;
}