package ru.nastya.entity;

import ru.nastya.model.AccountType;
import ru.nastya.model.Currency;

import java.math.BigDecimal;

public sealed abstract class AbstractAccount permits AccountCash, AccountSaving {

    public abstract Long getId();

    public abstract AccountType getType();

    public abstract String getCurrency();

    public final Currency getCurrencyEnum() {
        return Currency.fromString(getCurrency());
    }

    public abstract BigDecimal getAmount();
}
