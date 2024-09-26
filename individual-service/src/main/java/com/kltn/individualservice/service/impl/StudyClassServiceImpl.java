package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassCRU;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.entity.Teacher;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudyClassService;
import com.kltn.individualservice.util.WebUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudyClassServiceImpl implements StudyClassService {
    StudyClassRepository studyClassRepository;
    SemesterService semesterService;
    WebUtil webUtil;

    @Override
    @CacheEvict(value = "studyClasses", allEntries = true)
    public StudyClass create(StudyClassCRU request) {
        return studyClassRepository.save(createOrUpdateStudyClass(new StudyClass(), request));
    }

    @Override
    @CachePut(value = "studyClass", key = "#request.id")
    @CacheEvict(value = "studyClasses", allEntries = true)
    public StudyClass update(StudyClassCRU request) {
        StudyClass studyClass = studyClassRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Study class"));
        return studyClassRepository.save(createOrUpdateStudyClass(studyClass, request));
    }

    @Override
    @Cacheable(value = "studyClass", key = "#id")
    public StudyClass findById(Long id) {
        return studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
    }

    @Override
    @CachePut(value = "studyClass", key = "#id")
    @CacheEvict(value = "studyClasses", allEntries = true)
    public StudyClass delete(Long id) {
        StudyClass studyClass = studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
        studyClass.setIsActive(EntityStatus.DELETED);
        return studyClassRepository.save(studyClass);
    }

    private StudyClass createOrUpdateStudyClass(StudyClass studyClass, StudyClassCRU studyClassCRU) {
        Semester semester = semesterService.findById(studyClassCRU.getSemesterId());
        studyClass.setName(studyClassCRU.getName());
        studyClass.setSemester(semester);
        studyClass.setClassesOfWeek(studyClassCRU.getClassesOfWeek());
        studyClass.setTeacher(new Teacher(studyClassCRU.getTeacherId()));
        studyClass.setTotalStudent(studyClassCRU.getTotalStudent());
        studyClass.setSubject(new Subject(studyClassCRU.getSubjectId()));

        return studyClass;
    }

    @Override
    @Cacheable(value = "studyClasses", key = "#statuses")
    public List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses) {
        return studyClassRepository.findAllByIsActiveIn(statuses);
    }

    @Override
    public List<StudyClass> findStudyClassByStudent(Long semesterId) {
        Long userId = Long.parseLong(webUtil.getUserId());
        return studyClassRepository.findStudyClassByStudentAndSemester(userId, semesterId);
    }

    @Override
    @Cacheable(value = "studyClasses", key = "#request")
    public Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable) {
        return studyClassRepository.findAllByIsActiveIn(request, pageable);
    }
}
