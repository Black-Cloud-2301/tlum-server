package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.dto.request.StudentStudyClassRequest;
import com.kltn.individualservice.dto.response.ReportCard;
import com.kltn.individualservice.dto.response.SemesterAverageResponse;
import com.kltn.individualservice.entity.StudentStudyClass;

import java.util.List;

public interface StudentStudyClassService {
    StudentStudyClass create(String studyClassId);
    List<StudentStudyClass> update(List<StudentStudyClassRequest> request);
    List<StudentStudyClass> findAllByStudentAndSemester(GetStudentStudyClassesRequest request);
    StudentStudyClass delete(Long id);
    List<StudentStudyClass> findByStudyClass(Long studyClassId);
    List<StudentStudyClass> optimizeRegistration(Long semesterId);
    List<StudentStudyClass> findCurrentTimetable(Long studentId);
    List<ReportCard> findReportCard(Long semesterId, Long studentId);
    List<SemesterAverageResponse> findSemesterAverageByStudent(Long studentId);
}
