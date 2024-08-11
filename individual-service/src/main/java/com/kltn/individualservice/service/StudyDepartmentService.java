package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyDepartmentService {
    StudyDepartment getStudyDepartmentByCodeAndEntityStatus(String code, EntityStatus entityStatus);

    List<StudyDepartment> getStudyDepartments(List<EntityStatus> entityStatuses);

    Page<StudyDepartment> getStudyDepartments(List<EntityStatus> entityStatuses, Pageable pageable);

    StudyDepartment findById(Long studyDepartmentId);
}
