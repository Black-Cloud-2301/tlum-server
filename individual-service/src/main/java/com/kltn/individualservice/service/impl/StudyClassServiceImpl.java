package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCachePut;
import com.kltn.individualservice.annotation.redis.CustomCacheable;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassCRU;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.dto.response.CountStudentRegistered;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.entity.Teacher;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.redis.RedisKey;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudyClassService;
import com.kltn.individualservice.util.WebUtil;
import com.kltn.individualservice.util.dto.RegisterGraphColoringUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudyClassServiceImpl implements StudyClassService {
    StudyClassRepository studyClassRepository;
    SemesterService semesterService;
    WebUtil webUtil;

    @Override
    @CustomCacheEvict(key = RedisKey.STUDY_CLASSES, allEntries = true)
    public StudyClass create(StudyClassCRU request) {
        return studyClassRepository.save(createOrUpdateStudyClass(new StudyClass(), request));
    }

    @Override
    @CustomCachePut(key = RedisKey.STUDY_CLASS, field = "#request.id")
    @CustomCacheEvict(key = RedisKey.STUDY_CLASSES, allEntries = true)
    public StudyClass update(StudyClassCRU request) {
        StudyClass studyClass = studyClassRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Study class"));
        return studyClassRepository.save(createOrUpdateStudyClass(studyClass, request));
    }

    @Override
    @CustomCacheable(key = RedisKey.STUDY_CLASS, field = "#id")
    public StudyClass findById(Long id) {
        return studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
    }

    @Override
    @CustomCachePut(key = RedisKey.STUDY_CLASS, field = "#id")
    @CustomCacheEvict(key = RedisKey.STUDY_CLASSES, allEntries = true)
    public StudyClass delete(Long id) {
        StudyClass studyClass = studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
        studyClass.setIsActive(EntityStatus.DELETED);
        return studyClassRepository.save(studyClass);
    }

    @Override
    public List<CountStudentRegistered> countStudentRegistered(List<Long> studyClassIds) {
        return studyClassRepository.countStudentRegistered(studyClassIds);
    }

    private StudyClass createOrUpdateStudyClass(StudyClass studyClass, StudyClassCRU studyClassCRU) {
        Semester semester = semesterService.findById(studyClassCRU.getSemesterId());
        List<StudyClass> studyClassesCrossSemester = studyClassRepository.findStudyClassesCrossSemester(semester, studyClassCRU.getTeacherId());

        if(studyClassCRU.getId() != null) {
            studyClassesCrossSemester.removeIf(sc -> sc.getId().equals(studyClassCRU.getId()));
        }

        // Check for overlapping schedules
        try {
            RegisterGraphColoringUtil.checkForOverlappingSchedules(studyClassCRU.getClassesOfWeek(), studyClassesCrossSemester);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing class schedules or checking for overlaps", e);
        }

        studyClass.setName(studyClassCRU.getName());
        studyClass.setSemester(semester);
        studyClass.setClassesOfWeek(studyClassCRU.getClassesOfWeek());
        studyClass.setTeacher(new Teacher(studyClassCRU.getTeacherId()));
        studyClass.setTotalStudent(studyClassCRU.getTotalStudent());
        studyClass.setSubject(new Subject(studyClassCRU.getSubjectId()));

        return studyClass;
    }

    @Override
    @CustomCacheable(key = RedisKey.STUDY_CLASSES, field = "#statuses")
    public List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses) {
        return studyClassRepository.findAllByIsActiveIn(statuses);
    }

    @Override
    public List<StudyClass> findStudyClassByStudent(Long semesterId) {
        Long userId = Long.parseLong(webUtil.getUserId());
        return studyClassRepository.findStudyClassByStudentAndSemester(userId, semesterId);
    }

    @Override
    @CustomCacheable(key = RedisKey.STUDY_CLASSES, field = "#request")
    public Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable) {
        return studyClassRepository.findAllByIsActiveIn(request, pageable);
    }

    @Override
    public List<StudyClass> findStudyClassesForCurrentWeek(Long teacherId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return studyClassRepository.findStudyClassesForCurrentWeek(teacherId, startOfWeek, endOfWeek);
    }
}
