package com.kltn.authservice.business.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}
