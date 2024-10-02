package org.tlum.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificationController {

//    @MessageMapping("/send")
//    @SendTo("/topic/notifications")
//    public NotificationMessage send(NotificationMessage message) {
//        return message;
//    }

    @GetMapping
    public String get() {
        return "index";
    }
}
