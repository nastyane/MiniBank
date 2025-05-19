package ru.nastya.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.nastya.api.account.CashOutAccountResponse;
import ru.nastya.controller.AccountController;
import ru.nastya.entity.AbstractAccount;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CashOutAccountModelAssembler implements RepresentationModelAssembler<AbstractAccount, CashOutAccountResponse> {

    @Override
    public CashOutAccountResponse toModel(AbstractAccount account) {
        CashOutAccountResponse response = new CashOutAccountResponse(
                account.getId(),
                account.getAmount(),
                account.getCurrency()
        );

        response.add(linkTo(methodOn(AccountController.class).cashIn(null)).withRel("cash-in"));
        response.add(linkTo(methodOn(AccountController.class).cashOut(null)).withSelfRel());

        return response;
    }
}

