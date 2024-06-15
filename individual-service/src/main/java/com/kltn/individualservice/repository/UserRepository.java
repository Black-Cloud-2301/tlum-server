package com.kltn.individualservice.repository;

import com.kltn.individualservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT MAX(CAST(SUBSTRING(u.code, 2, LENGTH(u.code)) AS integer)) FROM User u WHERE u.code LIKE 'A%'")
    Integer findMaxNumberInCode();
}
