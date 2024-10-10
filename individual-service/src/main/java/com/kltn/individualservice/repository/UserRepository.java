package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(u.code, LENGTH(?1) + 1, LENGTH(u.code)) AS integer)), 0) FROM User u WHERE u.code LIKE CONCAT(?1, '%')")
    Integer findMaxNumberInCode(String code);

    @Query("SELECT s.id FROM Student s WHERE s.isActive = 1 AND s.status IN :statuses")
    List<Long> findStudentIdsByObject(List<StudentStatus> statuses);

    @Query("SELECT s.id FROM Teacher s WHERE s.isActive = 1 AND s.status IN :statuses")
    List<Long> findTeacherIdsByObject(List<EmployeeStatus> statuses);
}
