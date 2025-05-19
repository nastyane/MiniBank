package ru.nastya.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nastya.api.FailResponse;
import ru.nastya.api.TransferRequest;
import ru.nastya.api.TransferResponse;
import ru.nastya.exception.IncorrectValuesException;
import ru.nastya.exception.NotExistsException;
import ru.nastya.service.AccountService;
import ru.nastya.utils.Response;


@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final AccountService accountService;


    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> transferMoney(
            @RequestBody TransferRequest request) {
        try {
            accountService.transferMoney(
                    request.getFromAccountId(),
                    request.getToAccountId(),
                    request.getAmount(),
                    request.getDescription());
            return ResponseEntity.ok(new TransferResponse(request.getAmount()));
        } catch (NotExistsException e) {
            return Response.notFound(e);
        } catch (IncorrectValuesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(ex.getMessage()));
        }
    }


/*    @PostMapping("/{accountId}/history")
    public ResponseEntity<?> getHistory(@RequestBody HistoryRequest request){
        try {

        } catch (IncorrectValuesException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(e.getMessage()));
        }
    }*/
}