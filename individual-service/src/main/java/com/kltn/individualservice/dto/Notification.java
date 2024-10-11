package com.kltn.individualservice.dto;

import com.kltn.individualservice.constant.NotificationObject;
import com.kltn.individualservice.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String title;
    private String message;
    private Instant scheduledTime;
    private String link;
    private NotificationType type;
    private NotificationObject object;
}
