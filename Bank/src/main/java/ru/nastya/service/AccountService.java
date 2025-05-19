package ru.nastya.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nastya.entity.*;
import ru.nastya.exception.IncorrectValuesException;
import ru.nastya.exception.NotExistsException;
import ru.nastya.model.*;
import ru.nastya.repository.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static java.math.BigDecimal.valueOf;
import static ru.nastya.model.AccountType.CASH;
import static ru.nastya.model.AccountType.SAVING;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("RUB", "USD");

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;
    private final AccountCashRepository accountCashRepository;
    private final AccountSavingRepository savingsAccountRepository;

    private final TransactionRepository transactionRepository;
    private final TransactionsCashRepository transactionsCashRepository;
    private final TransactionAccountRepository transactionAccountRepository;

    private final CurrencyService currencyService;
    private final AccountSavingRepository accountSavingRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public AccountCash createAccount(long userId, String currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsException("Пользователь не найден"));

        AccountCash accountCash = new AccountCash();
        accountCash.setId(nextAccountId(CASH));
        accountCash.setUser(user);
        accountCash.setAmount(BigDecimal.ZERO);
        accountCash.setCurrency(normalizeCurrency(currency));

        return accountCashRepository.save(accountCash);
    }

    public void createDefaultUserAccount(User user) {
        AccountCash accountCash = new AccountCash();
        accountCash.setId(nextAccountId(CASH));
        accountCash.setUser(user);
        accountCash.setAmount(BigDecimal.ZERO);
        accountCash.setCurrency("RUB");

        accountCashRepository.save(accountCash);
    }

    public BigDecimal getCurrentSavingAccountPercent() {
        return valueOf(17, 0);
    }

    @Transactional
    public AccountSaving createSavingsAccount(long userId, String currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsException("Пользователь не найден"));

        String normalizedCurrency = normalizeCurrency(currency);

        AccountSaving account = new AccountSaving();
        account.setId(nextAccountId(SAVING));
        account.setUser(user);
        account.setAmount(valueOf(0));
        account.setCurrency(normalizedCurrency);
        account.setPercent(getCurrentSavingAccountPercent());

        return savingsAccountRepository.save(account);
    }

    @Transactional
    public AbstractAccount getAccountById(Long accountId) {
        return accountCashRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Счет не найден"));
    }

    @Transactional
    public AccountSaving getSavingAccountById(Long accountId) {
        return savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Счет не найден"));
    }


    @Transactional
    public AccountCash cashInAccountCash(Long accountId, long amount) {
        if (amount <= 0) {
            throw new IncorrectValuesException("Сумма пополнения должна быть положительной");
        }

        AccountCash account = accountCashRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Счет не найден"));

        Transaction t = nextTransaction(TransactionType.CASH);

        BigDecimal amountBD = valueOf(amount);

        transactionsCashRepository.save(new TransactionsCash(t.getId(), new Account(accountId, 0), amountBD, t.getTime()));
        account.setAmount(account.getAmount().add(amountBD));

        return accountCashRepository.save(account);
    }

    @Transactional
    public AbstractAccount cashOutAccountCash(Long accountId, long amount) {
        if (amount <= 0) {
            throw new IncorrectValuesException("Сумма снятия должна быть положительной");
        }
        AccountCash account = accountCashRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Счет не найден"));
        if (account.getAmount().compareTo(valueOf(amount)) < 0) {
            throw new IncorrectValuesException("Не достаточно средств для перевода");
        }
        if (account.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectValuesException("Не достаточно средств");
        }
        Transaction t = nextTransaction(TransactionType.CASH);

        BigDecimal amountBD = valueOf(amount);

        transactionsCashRepository.save(new TransactionsCash(t.getId(), new Account(accountId, 0), amountBD.negate(), t.getTime()));
        account.setAmount(account.getAmount().subtract(amountBD));

        return accountCashRepository.save(account);
    }

    @Transactional
    public AccountSaving cashInSavingAccount(Long accountId, long amount) {
        if (amount <= 0) {
            throw new IncorrectValuesException("Сумма пополнения должна быть положительной");
        }

        AccountSaving account = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Сберегательный счет не найден"));

        Transaction t = nextTransaction(TransactionType.CASH);
        BigDecimal amountBD = valueOf(amount);

        transactionsCashRepository.save(new TransactionsCash(t.getId(), new Account(accountId, 0), amountBD, t.getTime()));

        account.setAmount(account.getAmount().add(valueOf(amount)));
        return savingsAccountRepository.save(account);
    }

    @Transactional
    public AccountSaving cashOutSavingAccount(long accountId, long amount) {
        if (amount <= 0) {
            throw new IncorrectValuesException("Сумма пополнения должна быть положительной");
        }
        AccountSaving account = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new NotExistsException("Сберегательный счет не найден"));

        if (account.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectValuesException("Не достаточно средств");
        }

        if (account.getAmount().compareTo(valueOf(amount)) < 0) {
            throw new IncorrectValuesException("Не достаточно средств для перевода");
        }

        Transaction t = nextTransaction(TransactionType.CASH);
        BigDecimal amountBD = valueOf(amount);

        transactionsCashRepository.save(new TransactionsCash(t.getId(), new Account(accountId, 0), amountBD.negate(), t.getTime()));

        account.setAmount(account.getAmount().subtract(valueOf(amount)));
        return savingsAccountRepository.save(account);
    }

    private AbstractAccount cashOutAccount(long accountId, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new NotExistsException("Счет не найден");
        }
        Account account = accountOptional.get();
        switch (AccountType.fromType(account.getType())) {
            case CASH -> {
                AccountCash a = accountCashRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));

                if (a.getAmount().compareTo(amount) < 0) {
                    throw new IncorrectValuesException("Недостаточно средств на счете отправителя");
                }

                a.setAmount(a.getAmount().subtract(amount));
                return accountCashRepository.save(a);
            }
            case SAVING -> {
                AccountSaving a = accountSavingRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));

                if (a.getAmount().compareTo(amount) < 0) {
                    throw new IncorrectValuesException("Недостаточно средств на счете отправителя");
                }

                a.setAmount(a.getAmount().subtract(amount));
                return accountSavingRepository.save(a);
            }
            default -> {
                throw new IllegalStateException("impossible");
            }


        }
    }

    private AbstractAccount cashInCurrencyAccount(long accountId, Currency amountCurrency, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new NotExistsException("Счет не найден");
        }
        Account account = accountOptional.get();
        switch (AccountType.fromType(account.getType())) {
            case CASH -> {
                AccountCash a = accountCashRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));

                a.setAmount(a.getAmount().add(currencyService.convert(amountCurrency.getCurrency(), a.getCurrency(), amount).amount()));
                return accountCashRepository.save(a);
            }
            case SAVING -> {
                AccountSaving a = accountSavingRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));

                a.setAmount(a.getAmount().add(currencyService.convert(amountCurrency.getCurrency(), a.getCurrency(), amount).amount()));
                return accountSavingRepository.save(a);
            }
            default -> {
                throw new IllegalStateException("impossible");
            }
        }
    }

    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IncorrectValuesException("Нельзя переводить на тот же счет");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectValuesException("Некорректная сумма перевода");
        }


        AbstractAccount fromAccount = cashOutAccount(fromAccountId, amount);

        AbstractAccount toAccount = getAccount(toAccountId);
        CurrencyConverted converted = currencyService.convert(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
        cashInAccount(toAccount, converted.amount());

        Transaction t = nextTransaction(TransactionType.ACCOUNT);
        transactionAccountRepository.save(new TransactionsAccount(
                t.getId(),
                new Account(fromAccountId, 0),
                new Account(toAccountId, 0),
                description,
                amount,
                fromAccount.getCurrency(),
                toAccount.getCurrency(),
                converted.rate(),
                Instant.now()
        ));
        String message = String.format(
                "перевод %.2f %s со счета %d на счет %d",
                amount,
                fromAccount.getCurrency(),
                fromAccountId,
                toAccountId
        );

        NotificationEvent event = new NotificationEvent(
                fromAccountId,
                fromAccount instanceof AccountCash ? ((AccountCash) fromAccount).getUser().getId() : null,
                "TRANSFER",
                amount,
                fromAccount.getCurrency(),
                message
        );

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("bank-events", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void cashInAccount(AbstractAccount account, BigDecimal amount) {
        switch (account) {
            case AccountCash a -> {
                a.setAmount(a.getAmount().add(amount));
                accountCashRepository.save(a);
                Long accountId = a.getId();
                Long userId = a.getUser().getId();
                String currency = a.getCurrency();
                String message = String.format("пополнение счета %.2f %s", amount, currency);
                NotificationEvent event = new NotificationEvent(
                        accountId,
                        userId,
                        "DEPOSIT",
                        amount,
                        currency,
                        message
                );

                try {
                    String json = objectMapper.writeValueAsString(event);
                    kafkaTemplate.send("bank-events", json);
                }  catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


            }
            case AccountSaving a -> {
                a.setAmount(a.getAmount().add(amount));
                accountSavingRepository.save(a);
                String message = String.format("пополнение счета %.2f %s", amount, a.getCurrency());
                kafkaTemplate.send("bank-events", message);
            }
        }

    }

    private AbstractAccount getAccount(long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new NotExistsException("Счет не найден");
        }
        Account account = accountOptional.get();
        switch (AccountType.fromType(account.getType())) {
            case CASH -> {
                return accountCashRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));
            }
            case SAVING -> {
                return accountSavingRepository.findById(account.getId())
                        .orElseThrow(() -> new NotExistsException("Счет не найден"));
            }
            default -> throw new IllegalStateException("impossible");
        }
    }

    private static String normalizeCurrency(String currency) {
        String normalized = currency.trim().toUpperCase();
        if (!SUPPORTED_CURRENCIES.contains(normalized)) {
            throw new IncorrectValuesException("Неподдерживаемая валюта: " + currency);
        }
        return normalized;
    }

    private long nextAccountId(AccountType accountType) {
        Account a = new Account(null, accountType.getNumber());
        return accountRepository.save(a).getId();
    }

    private Transaction nextTransaction(TransactionType transactionType) {
        Transaction t = new Transaction(null, transactionType.getNumber(), Instant.now());
        return transactionRepository.save(t);
    }


}
