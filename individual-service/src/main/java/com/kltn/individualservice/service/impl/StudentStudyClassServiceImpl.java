package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCacheable;
import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.sharedto.constants.NotificationObject;
import com.kltn.sharedto.constants.NotificationType;
import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.dto.request.StudentStudyClassRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.kafka.NotificationProducer;
import com.kltn.individualservice.redis.RedisKey;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.repository.StudentStudyClassRepository;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.StudentStudyClassService;
import com.kltn.individualservice.util.WebUtil;
import com.kltn.individualservice.util.dto.RegisterGraphColoringUtil;
import com.kltn.individualservice.util.exception.CustomException;
import com.kltn.sharedto.NotificationDto;
import com.kltn.sharedto.UserNotificationDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentStudyClassServiceImpl implements StudentStudyClassService {
    StudentStudyClassRepository studentStudyClassRepository;
    StudentRepository studentRepository;
    StudyClassRepository studyClassRepository;
    WebUtil webUtil;
    NotificationProducer notificationProducer;

    @Override
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public StudentStudyClass create(String studyClassId) {
        Long userId = Long.parseLong(getUserId());
        Student student = studentRepository.findByIdAndIsActive(userId, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student")));
        StudyClass studyClass = studyClassRepository.findByIdAndIsActive(Long.parseLong(studyClassId), EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.class")));
        Optional<StudentStudyClass> studentStudyClass = studentStudyClassRepository.findByStudentAndStudyClassAndIsActive(student, studyClass, EntityStatus.ACTIVE);
        List<StudentStudyClass> registeredStudyClasses = studentStudyClassRepository.findAllByStudentId(userId, studyClass.getSemester().getId());

        if (registeredStudyClasses.size() >= 10) {
            throw new CustomException(I18n.getMessage("msg.error.register_class_limit"));
        }
        if (studentStudyClass.isPresent()) {
            throw new CustomException(I18n.getMessage("msg.error.already_register_class"));
        } else {
            return studentStudyClassRepository.save(new StudentStudyClass(student, studyClass));
        }
    }

    @Override
    @CustomCacheable(key = RedisKey.STUDENT_STUDY_CLASSES, field = "#request.studentId + '::' + #request.semesterId")
    public List<StudentStudyClass> findAllByStudentAndSemester(GetStudentStudyClassesRequest request) {
        return studentStudyClassRepository.findAllByStudentId(request.getStudentId(), request.getSemesterId());
    }

    public String getUserId() {
        return webUtil.getUserId();
    }

//    @Override
//    public List<StudentStudyClass> findAllByStudentAndSemester(GetStudentStudyClassesRequest request) {
//        Long userId = Long.parseLong(getUserId());
//        String cachekey = RedisKey.STUDENT_STUDY_CLASSES;
//        String cacheField = userId + "::" + request.getSemesterId();
//
//        // Check cache first
//        List<StudentStudyClass> cachedResult = (List<StudentStudyClass>) baseRedisService.hashGet(cacheKey, cacheField);
//        if (cachedResult != null) {
//            return cachedResult;
//        }
//
//        // Fetch from repository if not in cache
//        List<StudentStudyClass> result = studentStudyClassRepository.findAllByStudentId(userId, request.getSemesterId());
//
//        // Store result in cache
//        baseRedisService.hashSet(cacheKey, cacheField, result);
//
//        return result;
//    }

    @Override
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public StudentStudyClass delete(Long id) {
        StudentStudyClass studentStudyClass = studentStudyClassRepository.findByIdAndIsActive(id, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student_class_register")));
        studentStudyClass.setIsActive(EntityStatus.INACTIVE);
        return studentStudyClassRepository.save(studentStudyClass);
    }

    @Override
    public List<StudentStudyClass> findByStudyClass(Long studyClassId) {
        return studentStudyClassRepository.findByStudyClassId(studyClassId);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public List<StudentStudyClass> optimizeRegistration(Long semesterId) {
        Long userId = Long.parseLong(getUserId());
        List<StudyClass> studyClasses = studyClassRepository.findStudyClassByStudentAndSemester(userId, semesterId);
        List<StudyClass> fullClasses = studyClassRepository.findStudyClassFullStudent(semesterId);
        List<StudyClass> registeredStudyClasses = studentStudyClassRepository.findAllByStudentId(userId, semesterId).stream().map(StudentStudyClass::getStudyClass).toList();

        List<StudyClass> nonRegisteredClasses = new ArrayList<>(studyClasses);
        nonRegisteredClasses.removeAll(fullClasses);
        nonRegisteredClasses.removeAll(registeredStudyClasses);

        try {
            Student student = studentRepository.findByIdAndIsActive(userId, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student")));

            List<StudyClass> newStudyClasses = RegisterGraphColoringUtil.scheduleClasses(nonRegisteredClasses, registeredStudyClasses);

            // Save all new study classes
            List<StudentStudyClass> studentStudyClasses = newStudyClasses.stream()
                    .map(studyClass -> new StudentStudyClass(student, studyClass))
                    .toList();
            return studentStudyClassRepository.saveAll(studentStudyClasses);
        } catch (Exception e) {
            log.error("Error while optimizing registration", e);
        }
        return List.of();
    }

    @Override
    @CustomCacheable(key = RedisKey.STUDENT_STUDY_CLASSES, field = "#studentId")
    public List<StudentStudyClass> findCurrentTimetable(Long studentId) {
        return studentStudyClassRepository.findCurrentTimetable(studentId);
    }

    @Override
    @Transactional
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public List<StudentStudyClass> update(List<StudentStudyClassRequest> request) {
        List<StudentStudyClass> studentStudyClasses = studentStudyClassRepository.findAllById(
                request.stream().map(StudentStudyClassRequest::getId).toList()
        );

        List<Long> userIds = new ArrayList<>();
        NotificationDto notification = null;

        for (StudentStudyClass studentStudyClass : studentStudyClasses) {
            StudentStudyClassRequest matchingRequest = request.stream()
                    .filter(r -> r.getId().equals(studentStudyClass.getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Request not found for id: " + studentStudyClass.getId()));

            boolean isMiddleScoreUpdated = matchingRequest.getMiddleScore() != null && !matchingRequest.getMiddleScore().equals(studentStudyClass.getMiddleScore());
            boolean isFinalScoreUpdated = matchingRequest.getFinalScore() != null && !matchingRequest.getFinalScore().equals(studentStudyClass.getFinalScore());

            studentStudyClass.setMiddleScore(matchingRequest.getMiddleScore());
            studentStudyClass.setFinalScore(matchingRequest.getFinalScore());
            studentStudyClass.setAttendances(matchingRequest.getAttendances());

            if (isMiddleScoreUpdated || isFinalScoreUpdated) {
                String scoreType = isMiddleScoreUpdated ? "giữa kỳ" : "cuối kỳ";
                if (notification == null) {
                    notification = new NotificationDto(
                            "Điểm thi",
                            "Điểm thi " + scoreType + " " + studentStudyClass.getStudyClass().getSubject().getName() + " đã được cập nhật",
                            Instant.now(),
                            null,
                            NotificationType.AUTO_GENERATE,
                            NotificationObject.STUDENT
                    );
                }
                userIds.add(studentStudyClass.getStudent().getId());
            }
        }

        if (notification != null) {
            UserNotificationDto userNotification = new UserNotificationDto(userIds, notification);
            notificationProducer.sendNotification(userNotification);
        }

        return studentStudyClassRepository.saveAll(studentStudyClasses);
    }
}
