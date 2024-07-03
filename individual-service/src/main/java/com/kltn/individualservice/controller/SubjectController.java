package com.kltn.individualservice.controller;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.service.SubjectService;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    ResponseEntity<Object> getAllSubject(@RequestParam List<EntityStatus> statuses) {
        return ResponseUtils.getResponseEntity(subjectService.findAllByIsActiveIn(statuses));
    }
}
