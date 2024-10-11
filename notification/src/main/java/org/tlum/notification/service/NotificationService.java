package org.tlum.notification.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tlum.notification.dto.request.GetNotificationsRequest;
import org.tlum.notification.entity.Notification;
import org.tlum.notification.entity.UserNotification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);
    List<Notification> getNotifications(GetNotificationsRequest request);
    List<UserNotification> getNotificationsByUser(Long userId);
    void createUserNotifications(String dto);
    Page<Notification> getNotifications(GetNotificationsRequest request, Pageable pageable);
    List<UserNotification> readNotifications(List<Long> notificationIds, Long userId);
}