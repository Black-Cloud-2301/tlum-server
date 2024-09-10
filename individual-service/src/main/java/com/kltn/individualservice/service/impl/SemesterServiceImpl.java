package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SemesterRequest;
import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Major;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.SemesterRepository;
import com.kltn.individualservice.repository.SubjectRepository;
import com.kltn.individualservice.service.MajorService;
import com.kltn.individualservice.service.SemesterService;
import com.kltn.individualservice.service.SubjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "semesters", key = "#request")
    public List<Semester> getSemesters(SemesterRequest request) {
        return semesterRepository.findAllByIsActiveIn(request.getEntityStatuses());
    }

    @Override
    @Cacheable(value = "semesters", key = "#request")
    public Page<Semester> getSemesters(SemesterRequest request, Pageable pageable) {
        return semesterRepository.findAllByIsActiveIn(request, pageable);
    }

    @Override
    @CacheEvict(value = "semesters", allEntries = true)
    public Semester createSemester(Semester request) {
        return semesterRepository.save(request);
    }

    @Override
    @CacheEvict(value = "semesters", allEntries = true)
    public Semester updateSemester(Semester request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        Semester semester = semesterRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Subject"));

        modalMapper.map(request, semester);
        return semesterRepository.save(semester);
    }

    @Override
    @Cacheable(value = "semesters", key = "#id")
    public Semester findById(Long id) {
        return semesterRepository.findById(id).orElseThrow(() -> new NotFoundException("Semester"));
    }

    @Override
    @CacheEvict(value = "semesters", allEntries = true)
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id).orElseThrow(() -> new NotFoundException("Semester"));
        semester.setIsActive(EntityStatus.DELETED);
        semesterRepository.save(semester);
    }

    @Override
    public Semester findNextSemester() {
        return semesterRepository.findNextSemester();
    }
}
