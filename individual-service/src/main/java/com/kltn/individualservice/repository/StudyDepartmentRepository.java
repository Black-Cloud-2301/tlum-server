package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyDepartmentRepository extends JpaRepository<StudyDepartment, Long> {
    Optional<StudyDepartment> findByCode(String code);

    Optional<StudyDepartment> findByCodeAndIsActive(String code, EntityStatus entityStatus);

    List<StudyDepartment> findByIsActiveIn(List<EntityStatus> entityStatuses);

    Page<StudyDepartment> findByIsActiveIn(List<EntityStatus> entityStatuses, Pageable pageable);
}