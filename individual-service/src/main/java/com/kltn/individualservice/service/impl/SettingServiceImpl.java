package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SettingsRequest;
import com.kltn.individualservice.entity.Setting;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.SettingRepository;
import com.kltn.individualservice.service.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingServiceImpl implements SettingService {
    SettingRepository settingRepository;

    @Override
    @Cacheable(value = "settings", key = "#request")
    public List<Setting> search(SettingsRequest request) {
        return settingRepository.findAllByTableCode(request);
    }

    @Override
    @Cacheable(value = "settings", key = "#request")
    public Page<Setting> search(SettingsRequest request, Pageable pageable) {
        return settingRepository.findAllByTableCode(request,pageable);
    }

    @Override
    @Cacheable(value = "setting", key = "#request.tableCode + #request.code")
    public Setting findByTableAndCode(SettingsRequest request) {
        return settingRepository.findByTableCodeAndCodeAndIsActive(request.getTableCode(), request.getCode(), EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException("Setting not found"));
    }

    @Override
    @CacheEvict(value = "settings", allEntries = true)
    @CachePut(value = "setting", key = "#request.tableCode + #request.code")
    public Setting update(Setting request) {
        Setting settingDb = settingRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Setting not found"));
        settingDb.setTableName(request.getTableName());
        settingDb.setValue(request.getValue());
        return settingRepository.save(settingDb);
    }
}
