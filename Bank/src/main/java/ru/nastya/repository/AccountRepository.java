package ru.nastya.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
