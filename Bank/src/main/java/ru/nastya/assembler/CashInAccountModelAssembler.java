package ru.nastya.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.nastya.api.account.CashInAccountResponse;
import ru.nastya.controller.AccountController;
import ru.nastya.entity.AccountCash;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CashInAccountModelAssembler implements RepresentationModelAssembler<AccountCash, CashInAccountResponse> {

    @Override
    public CashInAccountResponse toModel(AccountCash account) {
        CashInAccountResponse model = new CashInAccountResponse(
                account.getId(),
                account.getAmount(),
                account.getCurrency()
        );

        model.add(linkTo(methodOn(AccountController.class).cashOut(null)).withRel("cash-out"));
        model.add(linkTo(methodOn(AccountController.class).cashIn(null)).withSelfRel());

        return model;
    }
}

