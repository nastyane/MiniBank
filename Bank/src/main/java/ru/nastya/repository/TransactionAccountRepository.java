package ru.nastya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.TransactionsAccount;

import java.util.List;

public interface TransactionAccountRepository extends JpaRepository<TransactionsAccount, Long> {
    List<TransactionsAccount> findByAccountFromIdOrAccountToId(Long accountFromId, Long accountToId);
}
