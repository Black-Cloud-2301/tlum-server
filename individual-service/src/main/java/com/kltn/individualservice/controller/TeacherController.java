package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetTeachersRequest;
import com.kltn.individualservice.service.TeacherService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    ResponseEntity<Object> getTeachers(GetTeachersRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(teacherService.getTeachers(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(teacherService.getTeachers(request));
        }
    }

    @PostMapping("/import")
    ResponseEntity<Object> importTeachers(@RequestParam("file") MultipartFile file) {
        return ResponseUtils.getResponseEntity(teacherService.importTeachers(file));
    }
}
