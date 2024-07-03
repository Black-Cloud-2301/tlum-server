package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.repository.SubjectRepository;
import com.kltn.individualservice.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public List<Subject> findAllByIsActiveIn(List<EntityStatus> statuses) {
        return subjectRepository.findAllByIsActiveIn(statuses);
    }
}
