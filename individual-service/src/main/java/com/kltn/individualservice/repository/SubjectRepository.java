package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByCodeIn(List<String> codes);
    List<Subject> findAllByIsActiveIn(List<EntityStatus> statuses);
    Page<Subject> findAllByIsActiveIn(List<EntityStatus> statuses, Pageable pageable);
}