import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nastya.exception.IncorrectValuesException;
import ru.nastya.exception.NotExistsException;
import ru.nastya.entity.User;
import ru.nastya.repository.*;
import ru.nastya.service.AccountService;
import ru.nastya.service.CurrencyService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private AccountCashRepository accountCashRepository;
    @Mock private AccountSavingRepository accountSavingRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private TransactionsCashRepository transactionsCashRepository;
    @Mock private CurrencyService currencyService;

    @InjectMocks
    private AccountService accountService;

    private final User testUser = new User(1L, "testUser", "John", "Doe", "Smith", "pass", "+123456789");


    @Test
    void createAccount_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotExistsException.class,
                () -> accountService.createAccount(1L, "USD"));
    }


    @Test
    void cashInAccountCash_NegativeAmount_ThrowsException() {
        assertThrows(IncorrectValuesException.class,
                () -> accountService.cashInAccountCash(1L, -100L));
    }


    @Test
    void getCurrentSavingAccountPercent_ReturnsCorrectValue() {
        assertEquals(new BigDecimal("17"), accountService.getCurrentSavingAccountPercent());
    }

}