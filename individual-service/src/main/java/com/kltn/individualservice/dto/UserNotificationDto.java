package com.kltn.individualservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserNotificationDto {

    private List<Long> userIds;

    private Notification notification;

    public UserNotificationDto(List<Long> userId, Notification notification) {
        this.userIds = userId;
        this.notification = notification;
    }
}
