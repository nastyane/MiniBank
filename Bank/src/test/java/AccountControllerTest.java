
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastya.Application;
import ru.nastya.api.account.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final long existingAccountId = 40L;

    @Test
    @Order(1)
    @DisplayName("POST /accounts/create — создать счёт")
    void testCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(22);
        request.setCurrency("USD");

        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(0));
    }


    @Test
    @Order(3)
    @DisplayName("GET /accounts/{id} — счёт не найден (404)")
    void testGetAccountNotFound() throws Exception {
        mockMvc.perform(get("/accounts/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(4)
    @DisplayName("POST /accounts/cash-in — пополнение счёта (успешно)")
    void testCashInAccountSuccess() throws Exception {
        CashInAccountRequest request = new CashInAccountRequest();
        request.setAccountId(existingAccountId);
        request.setAmount(100);

        mockMvc.perform(post("/accounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingAccountId))
                .andExpect(jsonPath("$.currency").value("RUB"))
                .andExpect(jsonPath("$.newBalance").value(100.00));
    }

    @Test
    @Order(5)
    @DisplayName("POST /accounts/cash-in — счёт не найден (404)")
    void testCashInNotFound() throws Exception {
        CashInAccountRequest request = new CashInAccountRequest();
        request.setAccountId(999999L);
        request.setAmount(100);

        mockMvc.perform(post("/accounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(6)
    @DisplayName("POST /accounts/cash-in — некорректная сумма (400)")
    void testCashInIncorrectAmount() throws Exception {
        CashInAccountRequest request = new CashInAccountRequest();
        request.setAccountId(existingAccountId);
        request.setAmount(-50); // отрицательная сумма

        mockMvc.perform(post("/accounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(7)
    @DisplayName("POST /accounts/cash-out — снятие со счёта (успешно)")
    void testCashOutAccountSuccess() throws Exception {
        CashOutAccountRequest request = new CashOutAccountRequest();
        request.setAccountId(existingAccountId);
        request.setAmount(100); // предположим, достаточно средств

        mockMvc.perform(post("/accounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingAccountId))
                .andExpect(jsonPath("$.currency").value("RUB"))
                .andExpect(jsonPath("$.newBalance").value(0));
    }


    @Test
    @Order(8)
    @DisplayName("POST /accounts/cash-out — счёт не найден (404)")
    void testCashOutNotFound() throws Exception {
        CashOutAccountRequest request = new CashOutAccountRequest();
        request.setAccountId(999999L);
        request.setAmount(10);

        mockMvc.perform(post("/accounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(9)
    @DisplayName("POST /accounts/cash-out — недостаточно средств или отрицательная сумма (400)")
    void testCashOutIncorrectAmount() throws Exception {
        CashOutAccountRequest request = new CashOutAccountRequest();
        request.setAccountId(existingAccountId);
        request.setAmount(-20); // неверная сумма

        mockMvc.perform(post("/accounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}

