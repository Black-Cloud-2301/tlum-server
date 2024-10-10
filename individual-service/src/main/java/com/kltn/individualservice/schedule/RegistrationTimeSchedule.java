package com.kltn.individualservice.schedule;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.Student;
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
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationTimeSchedule {
    final RegistrationTimeRepository registrationTimeRepository;
    final SemesterService semesterService;
    final StudentService studentService;

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
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                .plusDays(3)
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime endTime = startTime.plusHours(1);

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
            registrationTime.setStartTime(startTime.atZone(ZoneId.systemDefault()).toInstant());
            registrationTime.setEndTime(endTime.atZone(ZoneId.systemDefault()).toInstant());
            registrationTimes.add(registrationTime);
        }

        registrationTimeRepository.saveAll(registrationTimes);
        log.info("Process students register semester done with: {}", registrationTimes.size());
    }
}
