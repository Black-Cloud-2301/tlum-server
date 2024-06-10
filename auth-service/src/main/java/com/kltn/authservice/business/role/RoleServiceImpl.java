package com.kltn.authservice.business.role;

import com.kltn.authservice.utils.enums.EntityStatus;
import com.kltn.authservice.utils.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return null;
    }

    @Override
    public Role getRole(String id) {
        return null;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByNameAndIsActive(name, EntityStatus.ACTIVE).orElseThrow(() -> new CustomException("Role not found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return List.of();
    }
}
