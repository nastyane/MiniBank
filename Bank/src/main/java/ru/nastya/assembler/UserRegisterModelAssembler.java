package ru.nastya.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.nastya.api.user.UserRegisterResponse;
import ru.nastya.controller.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserRegisterModelAssembler implements RepresentationModelAssembler<UserRegisterResponse, UserRegisterResponse> {

    @Override
    public UserRegisterResponse toModel(UserRegisterResponse user) {
      //  user.add(linkTo(methodOn(UserController.class).registerUser(null)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).registerUser(null)).withRel("register-again"));
        return user;
    }
}

