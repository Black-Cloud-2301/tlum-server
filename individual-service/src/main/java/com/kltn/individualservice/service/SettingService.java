package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.SettingsRequest;
import com.kltn.individualservice.entity.Setting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SettingService {
    List<Setting> search(SettingsRequest request);
    Page<Setting> search(SettingsRequest request, Pageable pageable);
    Setting findByTableAndCode(SettingsRequest request);
    Setting update(Setting setting);
}
