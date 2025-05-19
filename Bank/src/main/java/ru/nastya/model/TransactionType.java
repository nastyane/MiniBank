package ru.nastya.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    CASH(1),
    ACCOUNT(2);

    private final int number;
}
