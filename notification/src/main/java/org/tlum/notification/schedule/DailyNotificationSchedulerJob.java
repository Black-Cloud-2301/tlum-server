package org.tlum.notification.schedule;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;
import org.tlum.notification.entity.UserNotification;
import org.tlum.notification.service.NotificationService;
import org.tlum.notification.webSocket.NotificationHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Setter
@Slf4j
@Component
public class DailyNotificationSchedulerJob implements Job {

    private NotificationHandler notificationHandler;

    @Override
    public void execute(JobExecutionContext context) {
        UserNotification userNotification = (UserNotification) context.getJobDetail().getJobDataMap().get("userNotification");
        notificationHandler.broadcastMessage(userNotification);
    }

    public static void scheduleDailyJob(NotificationService notificationService) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // Lấy danh sách thông báo từ NotificationService
            List<UserNotification> userNotifications = notificationService.getNotificationsByDate(LocalDate.now(ZoneOffset.UTC));

            for (UserNotification userNotification : userNotifications) {
                Instant scheduledTime = userNotification.getNotification().getScheduledTime();
                if (scheduledTime.isAfter(Instant.now())) {
                    JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                            .withIdentity("notificationJob" + userNotification.getId())
                            .build();

                    jobDetail.getJobDataMap().put("notificationService", notificationService);
                    jobDetail.getJobDataMap().put("userNotification", userNotification);

                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("notificationTrigger" + userNotification.getId())
                            .startAt(Date.from(scheduledTime))
                            .build();

                    scheduler.scheduleJob(jobDetail, trigger);
                }
            }

            // Lên lịch job hàng ngày để kiểm tra và lên lịch các thông báo mới
            JobDetail dailyJobDetail = JobBuilder.newJob(DailyNotificationSchedulerJob.class)
                    .withIdentity("dailyNotificationSchedulerJob")
                    .build();

            dailyJobDetail.getJobDataMap().put("notificationService", notificationService);

            Trigger dailyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("dailyNotificationSchedulerTrigger")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                    .build();

            scheduler.scheduleJob(dailyJobDetail, dailyTrigger);
        } catch (SchedulerException e) {
            log.error("Failed to schedule daily notification scheduler job: {}", e.getMessage());
        }
    }
}