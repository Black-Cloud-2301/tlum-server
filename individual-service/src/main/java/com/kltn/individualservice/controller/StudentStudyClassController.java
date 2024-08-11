package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.service.StudentStudyClassService;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/student-study-class")
public class StudentStudyClassController {
    private final StudentStudyClassService studentStudyClassService;

    @GetMapping
    ResponseEntity<Object> findAllBySemester(GetStudentStudyClassesRequest request) {
            return ResponseUtils.getResponseEntity(studentStudyClassService.findAllBySemester(request));
    }

//    @GetMapping("/{id}")
//    ResponseEntity<Object> findById(@PathVariable Long id) {
//        return ResponseUtils.getResponseEntity(studyClassService.findById(id));
//    }

    @PostMapping
    ResponseEntity<Object> create(@RequestBody String studyClassId) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.create(studyClassId));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> delete(@PathVariable Long id) {
        return ResponseUtils.getResponseEntity(studentStudyClassService.delete(id));
    }
}
