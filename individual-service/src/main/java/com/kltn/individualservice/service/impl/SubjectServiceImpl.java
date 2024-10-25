package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.redis.CustomCacheEvict;
import com.kltn.individualservice.annotation.redis.CustomCacheable;
import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Major;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.redis.RedisKey;
import com.kltn.individualservice.repository.SubjectRepository;
import com.kltn.individualservice.service.MajorService;
import com.kltn.individualservice.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    MajorService majorService;
    ModelMapper modelMapper;

    @Override
    @CustomCacheable(key = RedisKey.SUBJECTS, field = "#request")
    public List<Subject> getSubjects(SubjectsRequest request) {
        return subjectRepository.findAllByIsActiveIn(request.getEntityStatuses());
    }

    @Override
    @CustomCacheable(key = RedisKey.SUBJECTS, field = "#request")
    public Page<Subject> getSubjects(SubjectsRequest request, Pageable pageable) {
        return subjectRepository.findAllByIsActiveIn(request, pageable);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.SUBJECTS, allEntries = true)
    public Subject createSubject(Subject request) {
        findMajorsAndSubject(request);
        return subjectRepository.save(request);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.SUBJECTS, allEntries = true)
    public Subject updateSubject(Subject request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        Subject subject = subjectRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Subject"));
        subject.getMajors().clear();
        subject.getRequireSubjects().clear();
        findMajorsAndSubject(request);
        modelMapper.map(request, subject);
        return subjectRepository.save(subject);
    }

    @Override
    @CustomCacheable(key = RedisKey.SUBJECTS, field = "#subjectIds")
    public List<Subject> findAllById(List<Long> subjectIds) {
        return subjectRepository.findAllById(subjectIds);
    }

    @Override
    @CustomCacheEvict(key = RedisKey.SUBJECTS, allEntries = true)
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

//    @Override
//    public List<Subject> findAllSubjectCanRegister(Long studentId) {
//        return subjectRepository.findAllSubjectCanRegister(studentId);
//    }

    private void findMajorsAndSubject(Subject request) {
        if(request.getMajorIds() != null && !request.getMajorIds().isEmpty()) {
            List<Major> majors = majorService.getMajorByIds(request.getMajorIds());
            request.setMajors(majors);
        }
        if(request.getRequireSubjectIds() != null && !request.getRequireSubjectIds().isEmpty()) {
            List<Subject> requireSubjects = subjectRepository.findAllById(request.getRequireSubjectIds());
            request.setRequireSubjects(requireSubjects);
        }
    }
}
