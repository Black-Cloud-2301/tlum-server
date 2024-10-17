package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.dto.request.StudentStudyClassRequest;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.service.StudentStudyClassService;
import com.kltn.individualservice.util.WebUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/student-study-class")
public class StudentStudyClassController {
    StudentStudyClassService studentStudyClassService;
    WebUtil webUtil;

    @GetMapping
    ResponseEntity<Object> findAllBySemester(GetStudentStudyClassesRequest request) {
            request.setStudentId(Long.parseLong(webUtil.getUserId()));
            return ResponseUtils.getResponseEntity(studentStudyClassService.findAllByStudentAndSemester(request));
    }

//    @GetMapping("/{id}")
//    ResponseEntity<Object> findById(@PathVariable Long id) {
//        return ResponseUtils.getResponseEntity(studyClassService.findById(id));
//    }

    @GetMapping("/study-class")
    ResponseEntity<Object> findByStudyClass(@RequestParam Long studyClassId) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.findByStudyClass(studyClassId));
    }

    @GetMapping("/current-timetable")
    ResponseEntity<Object> findCurrentTimetable() {
        return ResponseUtils.getResponseEntity(studentStudyClassService.findCurrentTimetable(Long.parseLong(webUtil.getUserId())));
    }

    @GetMapping("/by-student")
    ResponseEntity<Object> findByStudentId(@RequestParam(required = false) Long studentId) {
        if (studentId == null) {
            studentId = Long.parseLong(webUtil.getUserId());
        }
        return ResponseUtils.getResponseEntity(studentStudyClassService.findCurrentTimetable(studentId));
    }

    @GetMapping("/semester-average")
    ResponseEntity<Object> findSemesterAverage() {
        return ResponseUtils.getResponseEntity(studentStudyClassService.findSemesterAverageByStudent(Long.parseLong(webUtil.getUserId())));
    }

    @GetMapping("/report-card")
    ResponseEntity<Object> findReportCard(@RequestParam Long semesterId) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.findReportCard(semesterId, Long.parseLong(webUtil.getUserId())));
    }

    @PostMapping
    ResponseEntity<Object> create(@RequestBody String studyClassId) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.create(studyClassId));
    }

    @PutMapping
    ResponseEntity<Object> update(@RequestBody List<StudentStudyClassRequest> request) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.update(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> delete(@PathVariable Long id) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.delete(id));
    }

    @GetMapping("/optimize")
    ResponseEntity<Object> optimizeRegistration(@RequestParam Long semesterId) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.optimizeRegistration(semesterId));
    }
}
