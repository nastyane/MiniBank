package ru.nastya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nastya.api.FailResponse;
import ru.nastya.api.savingAccount.*;
import ru.nastya.exception.IncorrectValuesException;
import ru.nastya.exception.NotExistsException;
import ru.nastya.entity.AccountSaving;
import ru.nastya.service.AccountService;
import ru.nastya.utils.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/savingAccounts")
public class SavingAccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody CreateSavingsAccountRequest request
    ) {
        try {
            AccountSaving account = accountService.createSavingsAccount(
                    request.getUserId(),
                    request.getCurrency()
            );
            return ResponseEntity.ok(new CreateSavingAccountResponse(account.getId(), account.getAmount(), account.getCurrency(), account.getPercent()));
        } catch (NotExistsException e) {
            return Response.notFound(e);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> get(@PathVariable long accountId) {
        try {
            AccountSaving account = accountService.getSavingAccountById(accountId);
            return ResponseEntity.ok(
                    new GetSavingAccountResponse(
                            account.getId(),
                            account.getAmount(),
                            account.getCurrency(),
                            account.getPercent()
                    )
            );
        } catch (NotExistsException e) {
            return Response.notFound(e);
        }
    }

    @PostMapping("/cash-in")
    public ResponseEntity<?> cashIn(@RequestBody CashInSavingAccountRequest request) {
        try {
            AccountSaving account = accountService.cashInSavingAccount(request.getAccountId(), request.getAmount());
            return ResponseEntity.ok(new CashInSavingAccountResponse(
                    account.getId(),
                    account.getAmount(),
                    account.getCurrency()));
        } catch (NotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(ex.getMessage()));
        } catch (IncorrectValuesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(ex.getMessage()));
        }
    }

    @PostMapping("/cash-out")
    public ResponseEntity<?> cashOut(@RequestBody CashOutSavingAccountRequest request) {
        try {
            AccountSaving account = accountService.cashOutSavingAccount(request.getAccountId(), request.getAmount());
            return ResponseEntity.ok(new CashOutSavingAccountResponse(
                    account.getId(),
                    account.getAmount(),
                    account.getCurrency()));
        } catch (NotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(ex.getMessage()));
        } catch (IncorrectValuesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(ex.getMessage()));
        }
    }
}
