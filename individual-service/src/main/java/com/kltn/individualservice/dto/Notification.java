package com.kltn.individualservice.dto;

import com.kltn.individualservice.constant.NotificationObject;
import com.kltn.individualservice.constant.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Notification {
    private Long id;
    private String title;
    private String message;
    private Instant scheduledTime;
    private String link;
    private NotificationType type;
    private NotificationObject object;

    public Notification(String title, String message, Instant scheduledTime, String link, NotificationType type, NotificationObject object) {
        this.title = title;
        this.message = message;
        this.scheduledTime = scheduledTime;
        this.link = link;
        this.type = type;
        this.object = object;
    }
}
