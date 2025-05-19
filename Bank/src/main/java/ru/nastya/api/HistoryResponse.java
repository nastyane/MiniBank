package ru.nastya.api;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Getter
@Setter

public class HistoryResponse {
    private String type;
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String description;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal exchangeRate;
    private Instant time;
}

