package com.kltn.individualservice.repository;

import com.kltn.individualservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByModuleInAndFunctionInAndActionIn(List<String> modules, List<String> functions, List<String> actions);
    Optional<Permission> findByModuleAndFunctionAndAction(String module, String function, String action);
}