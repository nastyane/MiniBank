package ru.nastya.api.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class CashInAccountResponse extends RepresentationModel<CashInAccountResponse> {
    private long id;
    private BigDecimal newBalance;
    private String currency;
}