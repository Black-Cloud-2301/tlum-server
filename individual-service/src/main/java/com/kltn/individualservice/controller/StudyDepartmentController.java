package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.StudyDepartmentsRequest;
import com.kltn.individualservice.service.StudyDepartmentService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/study-department")
@RequiredArgsConstructor
public class StudyDepartmentController {
    private final StudyDepartmentService studyDepartmentService;

    @GetMapping
    ResponseEntity<Object> getStudyDepartments(StudyDepartmentsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(studyDepartmentService.getStudyDepartments(request.getEntityStatuses(), pageable));
        } else {
            return ResponseUtils.getResponseEntity(studyDepartmentService.getStudyDepartments(request.getEntityStatuses()));
        }
    }
}
