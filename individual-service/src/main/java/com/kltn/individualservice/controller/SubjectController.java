package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.service.SubjectService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    ResponseEntity<Object> getSubjects(SubjectsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(subjectService.getSubjects(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(subjectService.getSubjects(request));
        }
    }

//    @GetMapping("/register")
//    ResponseEntity<Object> getSubjectsCanRegister(Long studentId) {
//        return ResponseUtils.getResponseEntity(subjectService.findAllSubjectCanRegister(studentId));
//    }

    @PostMapping
    ResponseEntity<Object> createSubject(@RequestBody Subject request) {
        return ResponseUtils.getResponseEntity(subjectService.createSubject(request));
    }

    @PutMapping
    ResponseEntity<Object> updateSubject(@RequestBody Subject request) {
        return ResponseUtils.getResponseEntity(subjectService.updateSubject(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseUtils.getResponseEntity("Delete success");
    }
}
