package org.tlum.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tlum.notification.schedule.DailyNotificationSchedulerJob;
import org.tlum.notification.service.NotificationService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class NotificationApplication {
    @Autowired
    private NotificationService notificationService;

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        DailyNotificationSchedulerJob.scheduleDailyJob(notificationService);
    }
}
