//package com.kltn.individualservice.controller;
//
//import com.kltn.individualservice.dto.request.AttendanceRequest;
//import com.kltn.individualservice.service.AttendanceService;
//import com.kltn.individualservice.util.dto.ResponseUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1/attendance")
//@RequiredArgsConstructor
//public class AttendanceController {
//    private final AttendanceService attendanceService;
//
//    @GetMapping("/study-class")
//    ResponseEntity<Object> findByStudyClass(@RequestParam Long studyClassId) {
//        return ResponseUtils.getResponseEntity(attendanceService.findByStudyClass(studyClassId));
//    }
//
//    @PostMapping
//    ResponseEntity<Object> save(@RequestBody AttendanceRequest request) {
//        return ResponseUtils.getResponseEntity(attendanceService.save(request));
//    }
//}
