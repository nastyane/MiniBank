package ru.nastya.api.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateAccountResponse extends RepresentationModel<CreateAccountResponse> {
    private long id;
    private BigDecimal balance;
    private String currency;
}
