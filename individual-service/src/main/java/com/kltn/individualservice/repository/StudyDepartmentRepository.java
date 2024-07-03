package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.StudyDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StudyDepartmentRepository extends JpaRepository<StudyDepartment, Long> {
    Optional<StudyDepartment> findByCode(String code);
    Optional<StudyDepartment> findByCodeAndIsActive(String code, EntityStatus entityStatus);
}