package ru.nastya.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.nastya.api.account.CreateAccountResponse;
import ru.nastya.controller.AccountController;
import ru.nastya.entity.AccountCash;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AccountModelAssembler extends RepresentationModelAssemblerSupport<AccountCash, CreateAccountResponse> {

    public AccountModelAssembler() {
        super(AccountController.class, CreateAccountResponse.class);
    }

    @Override
    public CreateAccountResponse toModel(AccountCash account) {
        CreateAccountResponse model = new CreateAccountResponse(
                account.getId(),
                account.getAmount(),
                account.getCurrency()
        );

        model.add(linkTo(methodOn(AccountController.class).get(account.getId())).withRel("self"));
        model.add(linkTo(methodOn(AccountController.class).cashIn(null)).withRel("cash-in"));
        model.add(linkTo(methodOn(AccountController.class).cashOut(null)).withRel("cash-out"));

        return model;
    }
}
