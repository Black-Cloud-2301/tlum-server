package org.tlum.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlum.notification.entity.UserNotification;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findAllByIdIn(List<Long> notificationsIds);
}
