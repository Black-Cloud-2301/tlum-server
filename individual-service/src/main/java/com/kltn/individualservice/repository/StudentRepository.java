package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s " +
            "FROM Student s " +
            "JOIN s.majors m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(s.user.firstname,' ',s.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.majorIds} IS NULL OR m.id IN :#{#request.majorIds}) " +
            "AND (:#{#request.statuses} IS NULL OR s.status IN :#{#request.statuses}) " +
            "GROUP BY s.id")
    List<Student> findByIsActiveInAndStatusIn(GetStudentsRequest request);

    @Query("SELECT s " +
            "FROM Student s " +
            "JOIN s.majors m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(s.user.firstname,' ',s.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.majorIds} IS NULL OR m.id IN :#{#request.majorIds}) " +
            "AND (:#{#request.statuses} IS NULL OR s.status IN :#{#request.statuses}) " +
            "GROUP BY s.id")
    Page<Student> findByIsActiveInAndStatusIn(GetStudentsRequest request, Pageable pageable);

    Optional<Student> findByIdAndIsActive(Long id, EntityStatus isActive);

    @Query("SELECT s " +
            "FROM Student s " +
            "WHERE s.isActive = 1 AND s.status IN :#{#request.statuses} " +
            "AND (:#{#request.semesterId} IS NULL OR s.id NOT IN (SELECT r.student.id FROM RegistrationTime r " +
            "WHERE r.semester.id = :#{#request.semesterId} AND r.endTime >= CURRENT_TIMESTAMP))")
    List<Student> findStudentsNotRegister(GetStudentsRequest request);

    @Query("SELECT s.student " +
            "FROM StudentStudyClass s " +
            "WHERE s.studyClass.id = :studyClassId AND s.isActive = 1")
    List<Student> findStudentByStudyClass(Long studyClassId);
}