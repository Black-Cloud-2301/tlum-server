package org.tlum.notification.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Định kỳ gửi thông báo mỗi 5 giây
    @Scheduled(fixedRate = 5000)
    public void sendNotification() {
        messagingTemplate.convertAndSend("/topic/notifications", "New Notification at " + System.currentTimeMillis());
    }
}
