package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetMajorsRequest;
import com.kltn.individualservice.dto.request.StudyDepartmentsRequest;
import com.kltn.individualservice.service.MajorService;
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
@RequestMapping("/v1/major")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping
    ResponseEntity<Object> getStudyDepartments(GetMajorsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(majorService.getMajors(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(majorService.getMajors(request));
        }
    }
}
