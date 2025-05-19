package ru.nastya.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nastya.model.AccountType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account_saving")
public final class AccountSaving extends AbstractAccount {
    @Id
    private Long id;
    private BigDecimal amount;
    private String currency;
    private BigDecimal percent;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Override
    public AccountType getType() {
        return AccountType.SAVING;
    }
}
