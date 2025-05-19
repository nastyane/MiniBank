package ru.nastya;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationRepository repository;

    @KafkaListener(
            topics = "bank-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleEvent(NotificationEvent event) {
        if (event.getAccountId() == null || event.getEventType() == null || event.getMessage() == null) {
            log.warn("Пропущены обязательные поля в событии: {}", event);
            return;
        }

        Notification notification;
        notification = Notification.builder()
                .accountId(event.getAccountId().toString())
                .eventType(event.getEventType())
                .amount(event.getAmount())
                .message(event.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(notification);
        log.info("Уведомление сохранено: {}", event.getMessage());
    }
}
