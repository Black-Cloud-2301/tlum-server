package com.kltn.individualservice.kafka;

import com.kltn.individualservice.dto.UserNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, UserNotification> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, UserNotification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(UserNotification notification) {
        kafkaTemplate.send("notification-topic", notification);
    }
}
