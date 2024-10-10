package org.tlum.notification.kafka;

import com.kltn.sharedto.UserNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.tlum.notification.service.NotificationService;

import java.util.List;

@Service
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(UserNotificationDto notificationDto) {
        log.info("Received notifications: {}", notificationDto);
        notificationService.createUserNotifications(notificationDto);
    }
}
