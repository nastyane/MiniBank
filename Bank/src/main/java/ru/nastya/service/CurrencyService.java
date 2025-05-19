package ru.nastya.service;

import org.springframework.stereotype.Service;
import ru.nastya.model.Currency;
import ru.nastya.model.CurrencyConverted;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyService {
    private final Map<Currency, Map<Currency, BigDecimal>> rates = Map.of(
            Currency.USD, Map.of(
                    Currency.RUB, BigDecimal.valueOf(87)
            ),
            Currency.RUB, Map.of(
                    Currency.USD, BigDecimal.valueOf(0.011494253)
            )
    );

    public CurrencyConverted convert(String from, String to, BigDecimal fromAmount) {
        return convert(Currency.fromString(from), Currency.fromString(to), fromAmount);
    }

    public CurrencyConverted convert(Currency from, Currency to, BigDecimal fromAmount) {
        if (from == to) {
            return new CurrencyConverted(fromAmount, BigDecimal.ONE);
        }

        Map<Currency, BigDecimal> toRate = rates.get(from);
        if (toRate == null) {
            throw new IllegalStateException(String.format("Cannot find rate for from %s to %s", from, to));
        }

        BigDecimal rate = toRate.get(to);
        if (rate == null) {
            throw new IllegalStateException(String.format("Cannot find rate for from %s to %s", from, to));
        }

        return new CurrencyConverted(fromAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP), rate);
    }
}
