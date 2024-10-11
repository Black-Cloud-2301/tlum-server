package org.tlum.notification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tlum.notification.entity.Notification;

import java.util.List;

@Data
@NoArgsConstructor
public class UserNotificationDto {
    private List<Long> userIds;
    private Notification notification;
}
