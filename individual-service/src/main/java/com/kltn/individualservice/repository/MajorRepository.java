package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByCodeAndIsActive(String code, EntityStatus entityStatus);
    List<Major> findByIsActiveIn(List<EntityStatus> isActive);
    Page<Major> findByIsActiveIn(List<EntityStatus> isActive, Pageable pageable);
}