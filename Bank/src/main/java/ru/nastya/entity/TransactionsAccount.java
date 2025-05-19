package ru.nastya.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions_account")
public class TransactionsAccount extends AbstractTransaction {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_from")
    private Account accountFrom;
    @ManyToOne
    @JoinColumn(name = "account_to")
    private Account accountTo;

    private String description;
    private BigDecimal amount;

    private String currencyFrom;

    private String currencyTo;

    private BigDecimal exchangeRate;

    private Instant time;

}
