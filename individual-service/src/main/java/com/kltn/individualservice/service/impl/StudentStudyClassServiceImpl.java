package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.repository.StudentStudyClassRepository;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.StudentStudyClassService;
import com.kltn.individualservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentStudyClassServiceImpl implements StudentStudyClassService {
    private final StudentStudyClassRepository studentStudyClassRepository;
    private final StudentRepository studentRepository;
    private final StudyClassRepository studyClassRepository;
    private final WebUtil webUtil;

    @Override
    public StudentStudyClass create(String studyClassId) {
        Long userId = Long.parseLong(webUtil.getUserId());
        Student student = studentRepository.findByIdAndIsActive(userId, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException("Student not found"));
        StudyClass studyClass = studyClassRepository.findByIdAndIsActive(Long.parseLong(studyClassId), EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException("Study class not found"));
        Optional<StudentStudyClass> studentStudyClass = studentStudyClassRepository.findByStudentAndStudyClassAndIsActive(student, studyClass, EntityStatus.ACTIVE);
        if (studentStudyClass.isPresent()) {
            throw new NotFoundException("You have already registered for this class");
        } else {
            return studentStudyClassRepository.save(new StudentStudyClass(student, studyClass));
        }
    }

    @Override
    public List<StudentStudyClass> findAllBySemester(GetStudentStudyClassesRequest request) {
        Long userId = Long.parseLong(webUtil.getUserId());
        return studentStudyClassRepository.findAllByStudentIdAndSemester(request, userId);
    }
}
