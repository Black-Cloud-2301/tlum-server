package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.SettingsRequest;
import com.kltn.individualservice.entity.Setting;
import com.kltn.individualservice.service.SettingService;
import com.kltn.individualservice.util.CommonUtil;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/setting")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingController {
    SettingService settingService;

    @GetMapping
    ResponseEntity<Object> getStudyDepartments(SettingsRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = CommonUtil.createPageable(request);
            return ResponseUtils.getResponseEntity(settingService.search(request, pageable));
        } else {
            return ResponseUtils.getResponseEntity(settingService.search(request));
        }
    }

    @GetMapping("/find-by-code")
    ResponseEntity<Object> findAllByTableAndCode(SettingsRequest request) {
        return ResponseUtils.getResponseEntity(settingService.findByTableAndCode(request));
    }

    @PutMapping
    ResponseEntity<Object> update(@RequestBody Setting request) {
        return ResponseUtils.getResponseEntity(settingService.update(request));
    }
}
