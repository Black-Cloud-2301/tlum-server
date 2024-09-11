package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.entity.StudentStudyClass;

import java.util.List;

public interface StudentStudyClassService {
    StudentStudyClass create(String studyClassId);
    List<StudentStudyClass> findAllByStudentAndSemester(GetStudentStudyClassesRequest request);
    StudentStudyClass delete(Long id);
}
