package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudyDepartmentRepository;
import com.kltn.individualservice.service.StudyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyDepartmentServiceImpl implements StudyDepartmentService {
    private final StudyDepartmentRepository studyDepartmentRepository;

    @Override
    public StudyDepartment getStudyDepartmentByCodeAndEntityStatus(String code, EntityStatus entityStatus) {
        return studyDepartmentRepository.findByCodeAndIsActive(code, entityStatus).orElseThrow(() -> new NotFoundException("Study department"));
    }
}
