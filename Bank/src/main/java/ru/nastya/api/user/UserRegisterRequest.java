package ru.nastya.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequest {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phone;
}