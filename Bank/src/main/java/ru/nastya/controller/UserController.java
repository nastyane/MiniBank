package ru.nastya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nastya.api.FailResponse;
import ru.nastya.api.user.UserRegisterRequest;
import ru.nastya.api.user.UserRegisterResponse;
import ru.nastya.assembler.UserRegisterModelAssembler;
import ru.nastya.entity.User;
import ru.nastya.exception.AllreadyExistsException;
import ru.nastya.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final UserRegisterModelAssembler registerAssembler;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest user) {
        try {
            User res = userService.registerUser(
                    user.getLogin(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPatronymic(),
                    user.getPassword(),
                    user.getPhone()
            );

            UserRegisterResponse rawResponse = new UserRegisterResponse(
                    res.getId(),
                    res.getLogin(),
                    res.getFirstName(),
                    res.getLastName()
            );

            UserRegisterResponse response = registerAssembler.toModel(rawResponse);
            return ResponseEntity.ok(response);
        } catch (AllreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new FailResponse(e.getMessage()));
        }
    }
}


