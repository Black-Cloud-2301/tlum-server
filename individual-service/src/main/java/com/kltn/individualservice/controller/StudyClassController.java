package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetStudyClassesRequest;
import com.kltn.individualservice.dto.request.StudyClassCRU;
import com.kltn.individualservice.service.StudyClassService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/study-class")
public class StudyClassController {
    private final StudyClassService studyClassService;

    @GetMapping
    ResponseEntity<Object> findAllByIsActiveIn(GetStudyClassesRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(studyClassService.findAllByIsActiveIn(request.getEntityStatuses(), pageable));
        } else {
            return ResponseUtils.getResponseEntity(studyClassService.findAllByIsActiveIn(request.getEntityStatuses()));
        }
    }

    @PostMapping
    ResponseEntity<Object> create(@RequestBody StudyClassCRU studyClass) {
        return ResponseUtils.getResponseEntity(studyClassService.create(studyClass));
    }

    @PutMapping
    ResponseEntity<Object> update(@RequestBody StudyClassCRU studyClass) {
        return ResponseUtils.getResponseEntity(studyClassService.update(studyClass));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> delete(@PathVariable Long id) {
        return ResponseUtils.getResponseEntity(studyClassService.delete(id));
    }
}
