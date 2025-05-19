package ru.nastya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.entity.AccountSaving;

public interface AccountSavingRepository extends JpaRepository<AccountSaving, Long> {
}
