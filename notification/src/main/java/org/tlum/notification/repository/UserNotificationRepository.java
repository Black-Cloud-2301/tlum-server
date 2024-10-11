package org.tlum.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tlum.notification.entity.UserNotification;

import java.time.Instant;
import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findAllByIdIn(List<Long> notificationsIds);

    @Query("SELECT un FROM UserNotification un WHERE un.notification.scheduledTime BETWEEN :fromDate AND :toDate")
    List<UserNotification> findByScheduledDate(Instant fromDate, Instant toDate);
}
