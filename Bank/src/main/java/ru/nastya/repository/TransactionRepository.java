package ru.nastya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
