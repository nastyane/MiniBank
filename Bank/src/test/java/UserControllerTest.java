
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastya.Application;
import ru.nastya.api.user.UserRegisterRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /auth/register — успешная регистрация")
    void testRegisterUserSuccess() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setLogin("lkjn");
        request.setFirstName("Иван");
        request.setLastName("Иванов");
        request.setPatronymic("Иванович");
        request.setPassword("password123");
        request.setPhone("+79991112233");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("lkjn"))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"));
    }

    @Test
    @DisplayName("POST /auth/register — пользователь уже существует (409 Conflict)")
    void testRegisterUserConflict() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setLogin("1lkjhg");
        request.setFirstName("Анна");
        request.setLastName("Петрова");
        request.setPatronymic("Сергеевна");
        request.setPassword("qwerty");
        request.setPhone("+79998887766");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}

