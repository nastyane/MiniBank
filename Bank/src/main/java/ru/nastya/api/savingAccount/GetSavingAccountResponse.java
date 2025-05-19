package ru.nastya.api.savingAccount;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GetSavingAccountResponse {
    private long id;
    private BigDecimal balance;
    private String currency;
    private BigDecimal percent;
}
