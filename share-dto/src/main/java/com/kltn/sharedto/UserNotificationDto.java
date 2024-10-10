package com.kltn.sharedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserNotificationDto {

    private List<Long> userIds;

    private NotificationDto notificationDto;

    public UserNotificationDto(List<Long> userId, NotificationDto notificationDto) {
        this.userIds = userId;
        this.notificationDto = notificationDto;
    }
}
