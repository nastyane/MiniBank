package ru.nastya.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nastya.exception.IncorrectValuesException;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    CASH(1),
    SAVING(2);

    private final int number;

    public static AccountType fromType(int type) {
        for (AccountType res : AccountType.values()) {
            if (res.getNumber() == type) {
                return res;
            }
        }
        throw new IncorrectValuesException("неизвестный тип счёта");
    }
}
