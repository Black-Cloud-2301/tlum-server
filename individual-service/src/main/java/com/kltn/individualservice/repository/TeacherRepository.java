package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetTeachersRequest;
import com.kltn.individualservice.dto.response.MonthlyClassCountResponse;
import com.kltn.individualservice.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findAllByIsActiveIn(List<EntityStatus> statuses);

    @Query("SELECT s " +
            "FROM Teacher s " +
            "JOIN s.studyDepartment m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(s.user.firstname,' ',s.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.studyDepartmentIds} IS NULL OR m.id IN :#{#request.studyDepartmentIds}) " +
            "AND (:#{#request.statuses} IS NULL OR s.status IN :#{#request.statuses}) " +
            "GROUP BY s.id")
    List<Teacher> findAllByIsActiveInAndStatusIn(GetTeachersRequest request);

    @Query("SELECT s " +
            "FROM Teacher s " +
            "JOIN s.studyDepartment m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.user.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(CONCAT(s.user.firstname,' ',s.user.lastname)) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.studyDepartmentIds} IS NULL OR m.id IN :#{#request.studyDepartmentIds}) " +
            "AND (:#{#request.statuses} IS NULL OR s.status IN :#{#request.statuses}) " +
            "GROUP BY s.id")
    Page<Teacher> findAllByIsActiveInAndStatusIn(GetTeachersRequest request, Pageable pageable);
}