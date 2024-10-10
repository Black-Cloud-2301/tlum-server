package org.tlum.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tlum.notification.dto.request.GetNotificationsRequest;
import org.tlum.notification.entity.Notification;
import org.tlum.notification.service.NotificationService;
import org.tlum.notification.utils.CommonUtil;
import org.tlum.notification.utils.ResponseUtils;
import org.tlum.notification.utils.WebUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final WebUtil webUtil;

    @GetMapping
    ResponseEntity<Object> getNotifications(GetNotificationsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(notificationService.getNotifications(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(notificationService.getNotifications(request));
        }
    }

    @GetMapping("/user")
    ResponseEntity<Object> getNotificationsByUser() {
        return ResponseUtils.getResponseEntity(notificationService.getNotificationsByUser(Long.parseLong(webUtil.getUserId())));
    }

    @PostMapping
    ResponseEntity<Object> createNotification(@RequestBody Notification notification) {
        return ResponseUtils.getResponseEntity(notificationService.createNotification(notification));
    }

    @PostMapping("/read")
    ResponseEntity<Object> readNotifications(@RequestBody List<Long> notificationIds) {
        return ResponseUtils.getResponseEntity(notificationService.readNotifications(notificationIds, Long.parseLong(webUtil.getUserId())));
    }

}
