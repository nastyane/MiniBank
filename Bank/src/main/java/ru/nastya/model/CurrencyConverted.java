package ru.nastya.model;

import java.math.BigDecimal;

public record CurrencyConverted(BigDecimal amount, BigDecimal rate) {
}
