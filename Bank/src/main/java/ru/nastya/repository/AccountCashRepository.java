package ru.nastya.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.AccountCash;

public interface AccountCashRepository extends JpaRepository<AccountCash, Long> {
}
