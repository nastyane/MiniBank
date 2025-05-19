package ru.nastya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nastya.repository.TransactionAccountRepository;
import ru.nastya.repository.TransactionCashRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionAccountRepository accountRepository;
    private final TransactionCashRepository cashRepository;

}

