package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassCRU;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyClassService {
    StudyClass create(StudyClassCRU studyClass);
    StudyClass update(StudyClassCRU studyClass);
    StudyClass findById(Long id);
    StudyClass delete(Long id);
    List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses);
    List<StudyClass> findStudyClassByStudent(Long studentId);
    Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable);
}
