package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.dto.request.StudentStudyClassRequest;
import com.kltn.individualservice.entity.StudentStudyClass;

import java.util.List;

public interface StudentStudyClassService {
    StudentStudyClass create(String studyClassId);
    List<StudentStudyClass> update(List<StudentStudyClassRequest> request);
    List<StudentStudyClass> findAllByStudentAndSemester(GetStudentStudyClassesRequest request);
    StudentStudyClass delete(Long id);
    List<StudentStudyClass> findByStudyClass(Long studyClassId);
}
