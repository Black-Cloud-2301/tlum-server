package com.kltn.individualservice.controller;

import com.kltn.individualservice.annotation.ActionPermission;
import com.kltn.individualservice.annotation.FunctionPermission;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.StudentRequestCRU;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.WebUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/student")
@RequiredArgsConstructor
//@FunctionPermission("INDIVIDUAL/STUDENT")
public class StudentController {

    private final StudentService studentService;
    private final WebUtil webUtil;

    @GetMapping
//    @ActionPermission("VIEW")
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
            return ResponseUtils.getResponseEntity(studentService.findById(id));
    }

    @GetMapping("/my-study-info")
    public ResponseEntity<Object> getMyInfo() {
        return ResponseUtils.getResponseEntity(studentService.getMyInfo(Long.parseLong(webUtil.getUserId())));
    }

    @GetMapping("/study-class")
    public ResponseEntity<Object> getStudentsByStudyClass(@RequestParam Long studyClassId) {
        return ResponseUtils.getResponseEntity(studentService.getStudentsByStudyClass(studyClassId));
    }

    @PostMapping
    @ActionPermission("CREATE")
    public ResponseEntity<Object> createStudent(@ModelAttribute StudentRequestCRU request) {
        return ResponseUtils.getResponseEntity(studentService.createStudent(request));
    }

    @PostMapping("/import")
//    @ActionPermission("IMPORT")
    public ResponseEntity<Object> importStudent(@RequestPart("file") MultipartFile file) {
        return ResponseUtils.getResponseEntity(studentService.importStudents(file));
    }
}
