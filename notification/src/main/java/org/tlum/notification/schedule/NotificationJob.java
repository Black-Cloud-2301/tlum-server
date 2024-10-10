package org.tlum.notification.schedule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;
import org.tlum.notification.entity.UserNotification;
import org.tlum.notification.webSocket.NotificationHandler;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Setter
@Component
@Slf4j
public class NotificationJob implements Job {

    private NotificationHandler notificationHandler;

    public NotificationJob() {
        // No-argument constructor
    }

    @Override
    public void execute(JobExecutionContext context) {
        UserNotification userNotification = (UserNotification) context.getJobDetail().getJobDataMap().get("userNotification");
        notificationHandler.broadcastMessage(userNotification);
    }

    public static void scheduleNotifications(NotificationHandler notificationHandler, List<UserNotification> userNotifications) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            for (UserNotification userNotification : userNotifications) {
                Instant scheduledTime = userNotification.getNotification().getScheduledTime();
                if (scheduledTime.isAfter(Instant.now())) {
                    JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                            .withIdentity("notificationJob" + userNotification.getId())
                            .build();

                    jobDetail.getJobDataMap().put("notificationHandler", notificationHandler);
                    jobDetail.getJobDataMap().put("userNotification", userNotification);

                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("notificationTrigger" + userNotification.getId())
                            .startAt(Date.from(scheduledTime))
                            .build();

                    scheduler.scheduleJob(jobDetail, trigger);
                }
            }
        } catch (SchedulerException e) {
            log.error("Failed to schedule notifications: {}", e.getMessage());
        }
    }
}