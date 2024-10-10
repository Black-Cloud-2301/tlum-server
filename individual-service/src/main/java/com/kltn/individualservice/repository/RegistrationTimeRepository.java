package com.kltn.individualservice.repository;


import com.kltn.individualservice.dto.request.RegistrationTimeRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RegistrationTimeRepository extends JpaRepository<RegistrationTime, Long> {

    @Query("SELECT rt FROM RegistrationTime rt " +
            "WHERE rt.isActive = 1 " +
            "AND rt.semester.id = :semesterId " +
            "AND (:#{#request.code} IS NULL OR LOWER(rt.student.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(rt.student.user.firstname,' ',rt.student.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%')))")
    List<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request);

    @Query("SELECT rt FROM RegistrationTime rt " +
            "WHERE rt.isActive = 1 " +
            "AND rt.semester.id = :semesterId " +
            "AND (:#{#request.code} IS NULL OR LOWER(rt.student.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(rt.student.user.firstname,' ',rt.student.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%')))")
    Page<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request, Pageable pageable);

    @Query("SELECT rt FROM RegistrationTime rt " +
            "WHERE rt.isActive = 1 " +
            "AND rt.student.id = :studentId " +
            "AND rt.endTime >= :now " +
            "ORDER BY rt.startTime DESC LIMIT 1")
    Optional<RegistrationTime> findByStudentId(Long studentId, Instant now);
}