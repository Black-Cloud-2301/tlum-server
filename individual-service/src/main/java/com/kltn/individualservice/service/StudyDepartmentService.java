package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;

public interface StudyDepartmentService {
    StudyDepartment getStudyDepartmentByCodeAndEntityStatus(String code, EntityStatus entityStatus);
}
