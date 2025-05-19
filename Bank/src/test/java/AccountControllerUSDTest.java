
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastya.Application;
import ru.nastya.api.account.CashInAccountRequest;
import ru.nastya.api.account.CashOutAccountRequest;
import ru.nastya.api.account.CreateAccountRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerUSDTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static long usdAccountId;

    @Test
    @Order(1)
    @DisplayName("POST /accounts/create — создать счёт в USD")
    void testCreateAccountUSD() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(22);
        request.setCurrency("USD");

        var result = mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(0))
                .andReturn();

        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        usdAccountId = responseJson.get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("GET /accounts/{id} — счёт не найден (404)")
    void testGetAccountNotFoundUSD() throws Exception {
        mockMvc.perform(get("/accounts/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(3)
    @DisplayName("POST /accounts/cash-in — пополнение счёта в USD (успешно)")
    void testCashInAccountUSD() throws Exception {
        CashInAccountRequest request = new CashInAccountRequest();
        request.setAccountId(usdAccountId);
        request.setAmount(100);

        mockMvc.perform(post("/accounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usdAccountId))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.newBalance").value(100.00));
    }

    @Test
    @Order(4)
    @DisplayName("POST /accounts/cash-in — счёт не найден (404)")
    void testCashInNotFoundUSD() throws Exception {
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
    @Order(5)
    @DisplayName("POST /accounts/cash-in — некорректная сумма (400)")
    void testCashInIncorrectAmountUSD() throws Exception {
        CashInAccountRequest request = new CashInAccountRequest();
        request.setAccountId(usdAccountId);
        request.setAmount(-50);

        mockMvc.perform(post("/accounts/cash-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(6)
    @DisplayName("POST /accounts/cash-out — снятие в USD (успешно)")
    void testCashOutAccountUSD() throws Exception {
        CashOutAccountRequest request = new CashOutAccountRequest();
        request.setAccountId(usdAccountId);
        request.setAmount(100); // должно хватить после cash-in

        mockMvc.perform(post("/accounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usdAccountId))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.newBalance").value(0.00));
    }

    @Test
    @Order(7)
    @DisplayName("POST /accounts/cash-out — счёт не найден (404)")
    void testCashOutNotFoundUSD() throws Exception {
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
    @Order(8)
    @DisplayName("POST /accounts/cash-out — отрицательная сумма (400)")
    void testCashOutIncorrectAmountUSD() throws Exception {
        CashOutAccountRequest request = new CashOutAccountRequest();
        request.setAccountId(usdAccountId);
        request.setAmount(-20);

        mockMvc.perform(post("/accounts/cash-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
