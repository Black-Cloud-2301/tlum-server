//package org.tlum.notification.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.tlum.notification.dto.NotificationMessage;
//
//@Service
//@Slf4j
//@EnableScheduling
//public class NotificationService {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Scheduled(cron = "0 * * * * *") // Chạy mỗi phút một lần
//    public void sendNotification() {
//        String message = "This is a scheduled message";
//        log.info("Sending notification {}", message);
//        messagingTemplate.convertAndSend("/topic/notifications", new NotificationMessage(message));
//    }
//}