
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastya.Application;
import ru.nastya.api.savingAccount.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class SavingAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /savingAccounts/create — создание накопительного счёта")
    void testCreateSavingAccount() throws Exception {
        CreateSavingsAccountRequest request = new CreateSavingsAccountRequest();
        request.setUserId(1L);
        request.setCurrency("USD");

        mockMvc.perform(post("/savingAccounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("POST /savingAccounts/cash-in — пополнение накопительного счёта")
    void testCashInSavingAccount() throws Exception {
        CashInSavingAccountRequest request = new CashInSavingAccountRequest();
        request.setAccountId(16L);
        request.setAmount(100);

        mockMvc.perform(post("/savingAccounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /savingAccounts/cash-out — снятие с накопительного счёта")
    void testCashOutSavingAccount() throws Exception {
        CashOutSavingAccountRequest request = new CashOutSavingAccountRequest();
        request.setAccountId(16L);
        request.setAmount(50);

        mockMvc.perform(post("/savingAccounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
