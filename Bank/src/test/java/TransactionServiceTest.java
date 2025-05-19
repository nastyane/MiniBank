import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nastya.repository.TransactionAccountRepository;
import ru.nastya.repository.TransactionCashRepository;
import ru.nastya.service.TransactionService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionAccountRepository accountRepository;

    @Mock
    private TransactionCashRepository cashRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void serviceShouldBeCreated() {
        assertNotNull(transactionService);
    }
}

