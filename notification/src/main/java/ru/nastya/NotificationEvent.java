package ru.nastya;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long accountId;
    private Long userId;
    private String eventType;
    private BigDecimal amount;
    private String currency;
    private String message;
}

