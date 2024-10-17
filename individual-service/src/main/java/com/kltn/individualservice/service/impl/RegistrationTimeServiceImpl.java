package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.RegistrationTimeRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.RegistrationTimeRepository;
import com.kltn.individualservice.service.RegistrationTimeService;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.util.WebUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @CacheEvict(value = "registrationTimes", allEntries = true)
    public RegistrationTime create(RegistrationTimeRequest request) {
        return registrationTimeRepository.save(mapRegistrationTime(request));
    }

    @Override
    @CacheEvict(value = "registrationTimes", allEntries = true)
    public List<RegistrationTime> saveWithCache(List<RegistrationTime> request) {
        return registrationTimeRepository.saveAll(request);
    }

    @Override
    @CacheEvict(value = "registrationTimes", allEntries = true)
    @CachePut(value = "registrationTime", key = "#request.id")
    public RegistrationTime update(RegistrationTimeRequest request) {
        RegistrationTime registrationTime = registrationTimeRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Registration Time"));
        modelMapper.map(mapRegistrationTime(request), registrationTime);
        return registrationTimeRepository.save(registrationTime);
    }

    @Override
    @Cacheable(value = "registrationTimes", key = "#request")
    public List<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request) {
        return registrationTimeRepository.searchBySemesterId(semesterId, request);
    }

    @Override
    @Cacheable(value = "registrationTimes", key = "#request")
    public Page<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request, Pageable pageable) {
        return registrationTimeRepository.searchBySemesterId(semesterId, request, pageable);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "registrationTimes", allEntries = true),
            @CacheEvict(value = "registrationTime", key = "#id")
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
