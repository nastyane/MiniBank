package ru.nastya.api.savingAccount;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class CashInSavingAccountResponse {
    private long id;
    private BigDecimal newBalance;
    private String currency;
}