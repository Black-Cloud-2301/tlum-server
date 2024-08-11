package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.repository.SubjectRepository;
import com.kltn.individualservice.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public List<Subject> getSubjects(SubjectsRequest request) {
        return subjectRepository.findAllByIsActiveIn(request.getEntityStatuses());
    }

    @Override
    public Page<Subject> getSubjects(SubjectsRequest request, Pageable pageable) {
        return subjectRepository.findAllByIsActiveIn(request.getEntityStatuses(), pageable);
    }

    @Override
    public List<Subject> findAllById(List<Long> subjectIds) {
        return subjectRepository.findAllById(subjectIds);
    }
}
