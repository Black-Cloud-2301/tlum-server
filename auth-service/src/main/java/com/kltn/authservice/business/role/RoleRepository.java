package com.kltn.authservice.business.role;

import com.kltn.authservice.utils.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameAndIsActive(String name, EntityStatus isActive);
}
