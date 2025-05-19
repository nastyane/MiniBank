package ru.nastya.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class UserRegisterResponse extends RepresentationModel<UserRegisterResponse> {
    private long id;
    private String login;
    private String firstName;
    private String lastName;

}
