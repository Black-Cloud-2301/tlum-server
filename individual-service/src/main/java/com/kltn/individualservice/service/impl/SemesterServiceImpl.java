package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCacheable;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SemesterRequest;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.redis.RedisKey;
import com.kltn.individualservice.repository.SemesterRepository;
import com.kltn.individualservice.service.SemesterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterServiceImpl implements SemesterService {
    SemesterRepository semesterRepository;
    ModelMapper modalMapper;

    @Override
    @CustomCacheable(key = RedisKey.SEMESTERS, field = "#request")
    public List<Semester> getSemesters(SemesterRequest request) {
        return semesterRepository.findAllByIsActiveIn(request.getEntityStatuses());
    }

    @Override
    @CustomCacheable(key = RedisKey.SEMESTERS, field = "#request")
    public Page<Semester> getSemesters(SemesterRequest request, Pageable pageable) {
        return semesterRepository.findAllByIsActiveIn(request, pageable);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.SEMESTERS, allEntries = true)
    public Semester createSemester(Semester request) {
        return semesterRepository.save(request);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public Semester updateSemester(Semester request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        Semester semester = semesterRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Subject"));

        modalMapper.map(request, semester);
        return semesterRepository.save(semester);
    }

    @Override
    @CustomCacheable(key = RedisKey.SEMESTERS, field = "#id")
    public Semester findById(Long id) {
        return semesterRepository.findById(id).orElseThrow(() -> new NotFoundException("Semester"));
    }

    @Override
    @CustomCacheEvict(key = RedisKey.STUDENT_STUDY_CLASSES, allEntries = true)
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new NotFoundException("Semester"));
        semester.setIsActive(EntityStatus.DELETED);
        semesterRepository.save(semester);
    }

    @Override
    public Semester findNextSemester() {
        return semesterRepository.findNextSemester().orElseThrow(() -> new NotFoundException("Semester"));
    }
}
