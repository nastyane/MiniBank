package ru.nastya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.TransactionsCash;

public interface TransactionsCashRepository extends JpaRepository<TransactionsCash, Long> {
}
