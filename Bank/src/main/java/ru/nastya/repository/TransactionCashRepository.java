package ru.nastya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.TransactionsCash;

import java.util.List;

public interface TransactionCashRepository extends JpaRepository<TransactionsCash, Long> {
    List<TransactionsCash> findByAccountId(Long accountId);
}
