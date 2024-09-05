package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyClassRepository extends JpaRepository<StudyClass, Long> {
    List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses);

    @Query("SELECT s " +
            "FROM StudyClass s " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.name} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.year} IS NULL OR s.year = :#{#request.year}) " +
            "AND (:#{#request.studentGroup} IS NULL OR s.studentGroup = :#{#request.studentGroup}) " +
            "AND (:#{#request.semester} IS NULL OR s.semester = :#{#request.semester}) " +
            "AND (:#{#request.subjectId} IS NULL OR s.subject.id = :#{#request.subjectId}) " +
            "AND (:#{#request.teacherId} IS NULL OR s.teacher.id = :#{#request.teacherId}) " +
            "GROUP BY s.id")
    Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable);

    Optional<StudyClass> findByIdAndIsActive(Long id, EntityStatus isActive);
}