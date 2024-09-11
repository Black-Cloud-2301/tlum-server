package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.SemesterRequest;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/semester")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;

    @GetMapping
    ResponseEntity<Object> getSemesters(SemesterRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(semesterService.getSemesters(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(semesterService.getSemesters(request));
        }
    }

//    @GetMapping("/next")
//    ResponseEntity<Object> findNextSemester() {
//        return ResponseUtils.getResponseEntity(semesterService.findNextSemester());
//    }

    @PostMapping
    ResponseEntity<Object> createSemester(@RequestBody Semester request) {
        return ResponseUtils.getResponseEntity(semesterService.createSemester(request));
    }

    @PutMapping
    ResponseEntity<Object> updateSemester(@RequestBody Semester request) {
        return ResponseUtils.getResponseEntity(semesterService.updateSemester(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseUtils.getResponseEntity("Delete success");
    }
}
