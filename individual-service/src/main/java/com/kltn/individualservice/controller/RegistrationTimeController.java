package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.RegistrationTimeRequest;
import com.kltn.individualservice.service.RegistrationTimeService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/registration-time")
@RequiredArgsConstructor
public class RegistrationTimeController {
    private final RegistrationTimeService registrationTimeService;

    @GetMapping("/{id}")
    ResponseEntity<Object> getRegistrationTime(@PathVariable Long id, RegistrationTimeRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(registrationTimeService.searchBySemesterId(id,request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(registrationTimeService.searchBySemesterId(id,request));
        }
    }

    @PostMapping
    ResponseEntity<Object> createRegistrationTime(@RequestBody RegistrationTimeRequest request) {
        return ResponseUtils.getResponseEntity(registrationTimeService.create(request));
    }

    @PutMapping
    ResponseEntity<Object> updateRegistrationTime(@RequestBody RegistrationTimeRequest request) {
        return ResponseUtils.getResponseEntity(registrationTimeService.update(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteRegistrationTime(@PathVariable Long id) {
        registrationTimeService.delete(id);
        return ResponseUtils.getResponseEntity("Delete success");
    }

    @GetMapping("/student")
    ResponseEntity<Object> getRegistrationTimeByStudent() {
        return ResponseUtils.getResponseEntity(registrationTimeService.findByStudent());
    }
}
