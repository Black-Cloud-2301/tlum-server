package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudyDepartmentRepository;
import com.kltn.individualservice.service.StudyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyDepartmentServiceImpl implements StudyDepartmentService {
    private final StudyDepartmentRepository studyDepartmentRepository;

    @Override
    public StudyDepartment getStudyDepartmentByCodeAndEntityStatus(String code, EntityStatus entityStatus) {
        return studyDepartmentRepository.findByCodeAndIsActive(code, entityStatus).orElseThrow(() -> new NotFoundException("Study department"));
    }

    @Override
    public List<StudyDepartment> getStudyDepartments(List<EntityStatus> entityStatuses) {
        return studyDepartmentRepository.findByIsActiveIn(entityStatuses);
    }

    @Override
    public Page<StudyDepartment> getStudyDepartments(List<EntityStatus> entityStatuses, Pageable pageable) {
        return studyDepartmentRepository.findByIsActiveIn(entityStatuses, pageable);
    }

    @Override
    public StudyDepartment findById(Long studyDepartmentId) {
        return studyDepartmentRepository.findById(studyDepartmentId).orElseThrow(() -> new NotFoundException("Study department"));
    }
}
