package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCachePut;
import com.kltn.individualservice.annotation.redis.CustomCacheable;
import com.kltn.individualservice.annotation.redis.CustomCaching;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.RegistrationTimeRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.redis.RedisKey;
import com.kltn.individualservice.repository.RegistrationTimeRepository;
import com.kltn.individualservice.service.RegistrationTimeService;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.util.WebUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationTimeServiceImpl implements RegistrationTimeService {
    final RegistrationTimeRepository registrationTimeRepository;
    final StudentService studentService;
    final SemesterService semesterService;
    final ModelMapper modelMapper;
    final WebUtil webUtil;

    @Override
    @CustomCacheEvict(key = RedisKey.REGISTRATION_TIMES, allEntries = true)
    public RegistrationTime create(RegistrationTimeRequest request) {
        return registrationTimeRepository.save(mapRegistrationTime(request));
    }

    @Override
    @CustomCacheEvict(key = RedisKey.REGISTRATION_TIMES, allEntries = true)
    public List<RegistrationTime> saveWithCache(List<RegistrationTime> request) {
        return registrationTimeRepository.saveAll(request);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.REGISTRATION_TIMES, allEntries = true)
    @CustomCachePut(key = "registrationTime", field = "#request.id")
    public RegistrationTime update(RegistrationTimeRequest request) {
        RegistrationTime registrationTime = registrationTimeRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Registration Time"));
        modelMapper.map(mapRegistrationTime(request), registrationTime);
        return registrationTimeRepository.save(registrationTime);
    }

    @Override
    @CustomCacheable(key = RedisKey.REGISTRATION_TIMES, field = "#request")
    public List<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request) {
        return registrationTimeRepository.searchBySemesterId(semesterId, request);
    }

    @Override
    @CustomCacheable(key = RedisKey.REGISTRATION_TIMES, field = "#request")
    public Page<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request, Pageable pageable) {
        return registrationTimeRepository.searchBySemesterId(semesterId, request, pageable);
    }

    @Override
    @CustomCaching(evict = {
            @CustomCacheEvict(key = RedisKey.REGISTRATION_TIMES, allEntries = true),
            @CustomCacheEvict(key = "registrationTime", field = "#id")
    })
    public void delete(Long id) {
        RegistrationTime registrationTime = registrationTimeRepository.findById(id).orElseThrow(() -> new NotFoundException("Registration Time"));
        registrationTime.setIsActive(EntityStatus.INACTIVE);
        registrationTimeRepository.save(registrationTime);
    }

    @Override
    public RegistrationTime findByStudent() {
        String userId = webUtil.getUserId();
        Instant currentTimestamp = Instant.now();
        return registrationTimeRepository.findByStudentId(Long.parseLong(userId), currentTimestamp)
                .orElseThrow(() -> new NotFoundException("Registration Time"));
    }

    private RegistrationTime mapRegistrationTime(RegistrationTimeRequest request) {
        RegistrationTime registrationTime = new RegistrationTime();
        registrationTime.setId(request.getId());
        registrationTime.setStartTime(request.getStartTime());
        registrationTime.setEndTime(request.getEndTime());
        registrationTime.setStudent(studentService.findById(request.getStudentId()));
        registrationTime.setSemester(semesterService.findById(request.getSemesterId()));
        return registrationTime;
    }
}
