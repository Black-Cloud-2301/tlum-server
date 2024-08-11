package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.service.SubjectService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
