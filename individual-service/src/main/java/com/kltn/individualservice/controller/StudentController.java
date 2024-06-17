package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.StudentRequestCRU;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Object> getStudents(GetStudentsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(studentService.getStudents(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(studentService.getStudents(request));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudents(@PathVariable Long id) {
            return ResponseUtils.getResponseEntity(studentService.getStudent(id));
    }

    @PostMapping
    public ResponseEntity<Object> createStudent(@ModelAttribute StudentRequestCRU request) {
        return ResponseUtils.getResponseEntity(studentService.createStudent(request));
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importStudent(@RequestPart("file") MultipartFile file) {
        return ResponseUtils.getResponseEntity(studentService.importStudents(file));
    }
}
