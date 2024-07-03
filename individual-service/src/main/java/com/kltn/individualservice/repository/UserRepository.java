package com.kltn.individualservice.repository;

import com.kltn.individualservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(u.code, LENGTH(?1) + 1, LENGTH(u.code)) AS integer)), 0) FROM User u WHERE u.code LIKE CONCAT(?1, '%')")
    Integer findMaxNumberInCode(String code);
}
