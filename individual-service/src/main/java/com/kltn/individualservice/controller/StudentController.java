package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CommonUtil commonUtil;

    @GetMapping()
    public ResponseEntity<Object> getStudents(GetStudentsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(studentService.getStudents(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(studentService.getStudents(request));
        }
    }
}
