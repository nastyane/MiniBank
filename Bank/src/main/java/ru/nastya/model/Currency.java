package ru.nastya.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nastya.exception.IncorrectValuesException;

@Getter
@AllArgsConstructor
public enum Currency {
    RUB("RUB"),
    USD("USD");

    private final String currency;

    public static Currency fromString(String currency) {
        for (Currency value : Currency.values()) {
            if (value.currency.equals(currency)) {
                return value;
            }
        }
        throw new IncorrectValuesException("Unknown currency: " + currency);
    }
}
