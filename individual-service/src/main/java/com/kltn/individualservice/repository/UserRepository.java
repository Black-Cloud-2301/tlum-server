package com.kltn.individualservice.repository;

import com.kltn.individualservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
