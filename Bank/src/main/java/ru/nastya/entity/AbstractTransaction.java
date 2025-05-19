package ru.nastya.entity;

import java.math.BigDecimal;

public abstract class AbstractTransaction {
    public abstract Long getId();

    public abstract BigDecimal getAmount();
}
