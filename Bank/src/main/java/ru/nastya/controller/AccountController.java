package ru.nastya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nastya.api.FailResponse;
import ru.nastya.api.account.*;
import ru.nastya.assembler.AccountModelAssembler;
import ru.nastya.assembler.CashInAccountModelAssembler;
import ru.nastya.assembler.CashOutAccountModelAssembler;
import ru.nastya.entity.AbstractAccount;
import ru.nastya.entity.AccountCash;
import ru.nastya.exception.IncorrectValuesException;
import ru.nastya.exception.NotExistsException;
import ru.nastya.service.AccountService;
import ru.nastya.utils.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountModelAssembler assembler;
    private final CashInAccountModelAssembler cashInAssembler;
    private final CashOutAccountModelAssembler cashOutAssembler;



    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody CreateAccountRequest request
    ) {
        try {
            AccountCash account = accountService.createAccount(request.getUserId(), request.getCurrency());
            CreateAccountResponse model = assembler.toModel(account);
            return ResponseEntity.ok(model);
        } catch (NotExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> get(@PathVariable long accountId) {
        try {
            AbstractAccount accountCash = accountService.getAccountById(accountId);
            return ResponseEntity.ok(
                    new GetAccountResponse(
                            accountCash.getId(),
                            accountCash.getAmount(),
                            accountCash.getCurrency()
                    )
            );
        } catch (NotExistsException e) {
            return Response.notFound(e);
        }
    }

    @PostMapping("/cash-in")
    public ResponseEntity<?> cashIn(@RequestBody CashInAccountRequest request) {
        try {
            AccountCash accountCash = accountService.cashInAccountCash(request.getAccountId(), request.getAmount());
            CashInAccountResponse response = cashInAssembler.toModel(accountCash);
            return ResponseEntity.ok(response);
        } catch (NotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(ex.getMessage()));
        } catch (IncorrectValuesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(ex.getMessage()));
        }
    }

    @PostMapping("/cash-out")
    public ResponseEntity<?> cashOut(@RequestBody CashOutAccountRequest request) {
        try {
            AbstractAccount account = accountService.cashOutAccountCash(request.getAccountId(), request.getAmount());
            CashOutAccountResponse response = cashOutAssembler.toModel(account);
            return ResponseEntity.ok(response);
        } catch (NotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(ex.getMessage()));
        } catch (IncorrectValuesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(ex.getMessage()));
        }
    }

}
