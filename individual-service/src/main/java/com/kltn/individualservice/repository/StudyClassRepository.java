package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyClassRepository extends JpaRepository<StudyClass, Long> {
    List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses);
    Page<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses, Pageable pageable);
}