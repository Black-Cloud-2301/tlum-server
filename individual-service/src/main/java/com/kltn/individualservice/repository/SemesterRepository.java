package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SemesterRequest;
import com.kltn.individualservice.entity.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findAllByIsActiveIn(List<EntityStatus> isActive);

    @Query("SELECT s " +
            "FROM Semester s " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.year} IS NULL OR s.year IN :#{#request.year}) " +
            "AND (:#{#request.studentGroup} IS NULL OR s.studentGroup IN :#{#request.studentGroup}) " +
            "AND (:#{#request.semester} IS NULL OR s.semester IN :#{#request.semester})")
    Page<Semester> findAllByIsActiveIn(SemesterRequest request, Pageable pageable);

    @Query("SELECT s FROM Semester s WHERE s.isActive = 1 AND s.fromDate > CURRENT_DATE ORDER BY s.fromDate ASC LIMIT 1")
    Semester findNextSemester();
}
