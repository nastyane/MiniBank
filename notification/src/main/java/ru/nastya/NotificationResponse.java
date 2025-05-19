package ru.nastya;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String accountId;
    private String eventType;
    private Double amount;
    private String message;
    private LocalDateTime createdAt;
}

