package ru.nastya.api.account;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class GetAccountResponse {
    private long id;
    private BigDecimal balance;
    private String currency;
}
