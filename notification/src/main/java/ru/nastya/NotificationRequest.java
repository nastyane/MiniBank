package ru.nastya;
import lombok.Data;

@Data
public class NotificationRequest {
    private String accountId;
    private String eventType;
    private Double amount;
    private String message;
}

