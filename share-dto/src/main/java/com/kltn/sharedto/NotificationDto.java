package com.kltn.sharedto;

import com.kltn.sharedto.constants.NotificationObject;
import com.kltn.sharedto.constants.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String title;
    private String message;
    private Instant scheduledTime;
    private String link;
    private NotificationType type;
    private NotificationObject object;
}
