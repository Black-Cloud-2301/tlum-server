package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByCodeIn(List<String> codes);
    List<Subject> findAllByIsActiveIn(List<EntityStatus> statuses);
    @Query("SELECT s " +
            "FROM Subject s " +
            "JOIN s.majors m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.majorIds} IS NULL OR m.id IN :#{#request.majorIds}) " +
            "GROUP BY s.id")
    Page<Subject> findAllByIsActiveIn(SubjectsRequest request, Pageable pageable);
}