package org.tlum.notification.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.tlum.notification.entity.UserNotification;
import org.tlum.notification.service.NotificationService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationHandler implements WebSocketHandler {
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper;
    @Setter
    private NotificationService notificationService;

    public NotificationHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        String userId = null;
        if (query != null && query.contains("userId=")) {
            userId = query.split("userId=")[1];
        }
        session.getAttributes().put("userId", userId);
        sessions.add(session);
        if (userId != null) {
            var notifications = notificationService.getNotificationsByUser(Long.parseLong(userId));
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(notifications)));
            } catch (Exception e) {
                log.error("Error sending message to session: {}", session.getId(), e);
            }
        }

        log.info("Connection established on session: {} with userId: {}", session.getId(), userId);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message received: {} on userId: {}", message.getPayload(), session.getAttributes().get("userId"));
        UserNotification notificationJson = objectMapper.readValue((String) message.getPayload(), UserNotification.class);
        broadcastMessage(notificationJson);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info("Exception occurred: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, CloseStatus closeStatus) {
        sessions.remove(session);
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcastMessage(UserNotification notification) {
        Map<Long, List<UserNotification>> notificationsByUserId = Collections.singletonMap(notification.getUserId(), List.of(notification));

        synchronizedBySessions(notificationsByUserId);
    }

    public void broadcastMessages(List<UserNotification> notifications) {

        Map<Long, List<UserNotification>> notificationsByUserId = notifications.stream()
                .collect(Collectors.groupingBy(UserNotification::getUserId));

        synchronizedBySessions(notificationsByUserId);
    }

    private void synchronizedBySessions(Map<Long, List<UserNotification>> notificationsByUserId) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                String userIdStr = (String) session.getAttributes().get("userId");
                if (userIdStr != null) {
                    Long userId = Long.parseLong(userIdStr);
                    List<UserNotification> userNotifications = notificationsByUserId.get(userId);
                    if (userNotifications != null && !userNotifications.isEmpty()) {
                        try {
                            String message = objectMapper.writeValueAsString(userNotifications);
                            session.sendMessage(new TextMessage(message));
                        } catch (Exception e) {
                            log.error("Error sending message to session: {}", session.getId(), e);
                        }
                    }
                }
            }
        }
    }
}