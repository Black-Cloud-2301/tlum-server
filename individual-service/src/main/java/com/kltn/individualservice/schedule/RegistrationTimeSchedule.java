package com.kltn.individualservice.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.NotificationObject;
import com.kltn.individualservice.constant.NotificationType;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.dto.Notification;
import com.kltn.individualservice.dto.UserNotificationDto;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.kafka.NotificationProducer;
import com.kltn.individualservice.repository.RegistrationTimeRepository;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationTimeSchedule {
    final RegistrationTimeRepository registrationTimeRepository;
    final SemesterService semesterService;
    final StudentService studentService;
    final NotificationProducer notificationProducer;
    final ObjectMapper objectMapper;

    @Scheduled(cron = "${scheduling.cron.processStudentsRegisterSemester}")
    public void processStudentsRegisterSemester() {
        Semester semester = semesterService.findNextSemester();
        GetStudentsRequest request = new GetStudentsRequest();
        request.setStatuses(List.of(StudentStatus.STUDYING, StudentStatus.REGISTERED));
        request.setEntityStatuses(List.of(EntityStatus.ACTIVE));
        request.setSemesterId(semester.getId());
        List<Student> students = studentService.getStudentsNotRegister(request);
        List<RegistrationTime> registrationTimes = new ArrayList<>();

        int groupSize = 50;
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.now(), vietnamZoneId)
                .plusDays(3)
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<LocalDateTime, List<Long>> notificationMap = new HashMap<>();

        for (int i = 0; i < students.size(); i++) {
            if (i % groupSize == 0 && i != 0) {
                startTime = startTime.plusHours(1);
                endTime = endTime.plusHours(1);
            }
            if (startTime.getHour() > 21) {
                startTime = startTime.plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
                endTime = startTime.plusHours(1);
            }
            RegistrationTime registrationTime = new RegistrationTime();
            registrationTime.setStudent(students.get(i));
            registrationTime.setSemester(semester);
            registrationTime.setStartTime(startTime.atZone(vietnamZoneId).toInstant());
            registrationTime.setEndTime(endTime.atZone(vietnamZoneId).toInstant());
            registrationTimes.add(registrationTime);

            notificationMap.computeIfAbsent(startTime, k -> new ArrayList<>()).add(students.get(i).getId());
        }

        registrationTimeRepository.saveAll(registrationTimes);

        for (Map.Entry<LocalDateTime, List<Long>> entry : notificationMap.entrySet()) {
            LocalDateTime notificationStartTime = entry.getKey();
            List<Long> studentIds = entry.getValue();
            Notification notification = new Notification(
                    "Đăng ký học kỳ",
                    "Chuẩn bị đăng ký học kỳ mới",
                    notificationStartTime.minusDays(3).toInstant(vietnamZoneId.getRules().getOffset(notificationStartTime)),
                    "/student/register-for-study",
                    NotificationType.AUTO_GENERATE,
                    NotificationObject.STUDENT
            );
            UserNotificationDto userNotificationDto = new UserNotificationDto(studentIds, notification);
            try {
                notificationProducer.sendNotification(objectMapper.writeValueAsString(userNotificationDto));
            } catch (Exception e) {
                log.error("Error when send notification: {}", e.getMessage());
            }
        }
    }
}
