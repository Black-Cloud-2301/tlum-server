package com.kltn.individualservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNotification  {
    private Long id;

    private Long userId;

    private Notification notification;

    private boolean read;

    public UserNotification(Long userId, Notification notification) {
        this.userId = userId;
        this.notification = notification;
        read = false;
    }
}
