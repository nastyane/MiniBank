package ru.nastya;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountId(String accountId);
}

