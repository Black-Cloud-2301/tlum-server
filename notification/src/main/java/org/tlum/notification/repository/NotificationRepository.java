package org.tlum.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tlum.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n JOIN UserNotification u WHERE u.userId = :userId AND n.sent = false AND n.scheduledTime <= CURRENT_TIMESTAMP")
    List<Notification> findNotificationsByUser(Long userId);
}
