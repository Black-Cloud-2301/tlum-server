package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findAllByIsActiveIn(List<EntityStatus> statuses);

    List<Teacher> findAllByIsActiveInAndStatusIn(List<EntityStatus> isActives, List<EmployeeStatus> statuses);

    Page<Teacher> findAllByIsActiveInAndStatusIn(List<EntityStatus> isActives, List<EmployeeStatus> statuses, Pageable pageable);
}