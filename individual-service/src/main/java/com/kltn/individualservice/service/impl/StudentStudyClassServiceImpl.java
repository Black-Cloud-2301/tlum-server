package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.repository.StudentStudyClassRepository;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudentStudyClassService;
import com.kltn.individualservice.util.WebUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentStudyClassServiceImpl implements StudentStudyClassService {
   StudentStudyClassRepository studentStudyClassRepository;
   StudentRepository studentRepository;
   StudyClassRepository studyClassRepository;
   WebUtil webUtil;

    @Override
    @CacheEvict(value = "studentStudyClasses", allEntries = true)
    public StudentStudyClass create(String studyClassId) {
        Long userId = Long.parseLong(webUtil.getUserId());
        Student student = studentRepository.findByIdAndIsActive(userId, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student")));
        StudyClass studyClass = studyClassRepository.findByIdAndIsActive(Long.parseLong(studyClassId), EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.class")));
        Optional<StudentStudyClass> studentStudyClass = studentStudyClassRepository.findByStudentAndStudyClassAndIsActive(student, studyClass, EntityStatus.ACTIVE);
        if (studentStudyClass.isPresent()) {
            throw new NotFoundException(I18n.getMessage("msg.error.already_register_class"));
        } else {
            return studentStudyClassRepository.save(new StudentStudyClass(student, studyClass));
        }
    }

    @Override
    @Cacheable(value = "studentStudyClasses", key = "#request")
    public List<StudentStudyClass> findAllBySemester(GetStudentStudyClassesRequest request) {
        Long userId = Long.parseLong(webUtil.getUserId());
        return studentStudyClassRepository.findAllByStudentId(userId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "studentStudyClasses", allEntries = true),
            @CacheEvict(value = "studentStudyClass", key = "#id")
    })
    public StudentStudyClass delete(Long id) {
        StudentStudyClass studentStudyClass = studentStudyClassRepository.findByIdAndIsActive(id, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student_class_register")));
        studentStudyClass.setIsActive(EntityStatus.INACTIVE);
        return studentStudyClassRepository.save(studentStudyClass);
    }
}
