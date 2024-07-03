package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllByIsActiveIn(List<EntityStatus> statuses);
}
