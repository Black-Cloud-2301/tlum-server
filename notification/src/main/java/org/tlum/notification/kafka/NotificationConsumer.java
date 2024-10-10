package org.tlum.notification.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.tlum.notification.entity.UserNotification;
import org.tlum.notification.webSocket.NotificationHandler;

@Service
@Slf4j
public class NotificationConsumer {

    private final NotificationHandler notificationHandler;

    public NotificationConsumer(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(UserNotification notification) {
        log.info("Received notification: {}", notification);
        notificationHandler.broadcastMessage(notification);
    }
}
