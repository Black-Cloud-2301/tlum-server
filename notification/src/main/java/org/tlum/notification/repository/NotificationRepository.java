package org.tlum.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tlum.notification.entity.Notification;
import org.tlum.notification.entity.UserNotification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT u FROM Notification n JOIN UserNotification u ON n.id = u.notification.id WHERE u.userId = :userId AND n.scheduledTime <= CURRENT_TIMESTAMP")
    List<UserNotification> findNotificationsByUser(Long userId);

    @Query("SELECT n FROM Notification n WHERE n.isActive = 1 AND n.type=1 ORDER BY n.scheduledTime DESC")
    Page<Notification> findAllManual(Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.isActive = 1 AND n.type=1 ORDER BY n.scheduledTime DESC")
    List<Notification> findAllManual();
}
