package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByIsActiveInAndStatusIn(List<EntityStatus> isActive, List<StudentStatus> status);
}