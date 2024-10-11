package org.tlum.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.tlum.notification.constant.NotificationObject;
import org.tlum.notification.dto.UserNotificationDto;
import org.tlum.notification.dto.request.GetNotificationsRequest;
import org.tlum.notification.dto.response.BaseResponseTemplate;
import org.tlum.notification.entity.Notification;
import org.tlum.notification.entity.UserNotification;
import org.tlum.notification.feignClient.UserServiceClient;
import org.tlum.notification.repository.NotificationRepository;
import org.tlum.notification.repository.UserNotificationRepository;
import org.tlum.notification.schedule.NotificationJob;
import org.tlum.notification.service.NotificationService;
import org.tlum.notification.webSocket.NotificationHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserServiceClient userServiceClient;
    private final HttpServletRequest request;
    private final NotificationHandler notificationHandler;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        var result = notificationRepository.save(notification);
            List<Long> userIds = getUserIdsByObject(notification.getObject());
            List<UserNotification> userNotifications = userIds.stream().map(userId -> new UserNotification(userId, notification)).toList();
            List<UserNotification> savedUserNotifications = userNotificationRepository.saveAll(userNotifications);
        if (!notification.getScheduledTime().isAfter(Instant.now())) {
            notificationHandler.broadcastMessages(savedUserNotifications);
        } else {
            NotificationJob.scheduleNotifications(notificationHandler, savedUserNotifications);
        }
        return result;
    }

    @Override
    @Transactional
    public void createUserNotifications(String notificationDto) {
        try {
            UserNotificationDto dto = objectMapper.readValue(notificationDto, UserNotificationDto.class);
            Notification notification = notificationRepository.save(dto.getNotification());

            List<UserNotification> userNotifications = new ArrayList<>();

            for (Long userId : dto.getUserIds()) {
                UserNotification userNotification = new UserNotification(userId, notification);
                userNotifications.add(userNotification);
            }

            userNotifications = userNotificationRepository.saveAll(userNotifications);

            List<UserNotification> immediateNotifications = new ArrayList<>();
            List<UserNotification> scheduledNotifications = new ArrayList<>();

            for (UserNotification userNotification : userNotifications) {
                if (!notification.getScheduledTime().isAfter(Instant.now())) {
                    immediateNotifications.add(userNotification);
                } else {
                    scheduledNotifications.add(userNotification);
                }
            }

            if (!immediateNotifications.isEmpty()) {
                notificationHandler.broadcastMessages(immediateNotifications);
            }

            if (!scheduledNotifications.isEmpty()) {
                NotificationJob.scheduleNotifications(notificationHandler, scheduledNotifications);
            }
        } catch (Exception e) {
            log.error("Error deserializing UserNotificationDto", e);
        }
    }

    @Override
    public List<Notification> getNotifications(GetNotificationsRequest request) {
        return notificationRepository.findAll();
    }

    @Override
    public List<UserNotification> getNotificationsByUser(Long userId) {
        return notificationRepository.findNotificationsByUser(userId);
    }

    @Override
    public Page<Notification> getNotifications(GetNotificationsRequest request, Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public List<UserNotification> readNotifications(List<Long> notificationIds, Long userId) {
        List<UserNotification> userNotifications = userNotificationRepository.findAllByIdIn(notificationIds);
        userNotifications.forEach(userNotification -> userNotification.setRead(true));
        userNotificationRepository.saveAll(userNotifications);
        return getNotificationsByUser(userId);
    }

    @Override
    public List<UserNotification> getNotificationsByDate(LocalDate date) {
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return userNotificationRepository.findByScheduledDate(startOfDay, endOfDay);
    }

    private List<Long> getUserIdsByObject(NotificationObject object) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        BaseResponseTemplate<List<Long>> response = userServiceClient.getUserIdByObject(header, object);
        return response.getData();
    }
}
