package com.kltn.individualservice.kafka;

import com.kltn.sharedto.UserNotificationDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, UserNotificationDto> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, UserNotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(UserNotificationDto notification) {
        kafkaTemplate.send("notification-topic", notification);
    }
}
