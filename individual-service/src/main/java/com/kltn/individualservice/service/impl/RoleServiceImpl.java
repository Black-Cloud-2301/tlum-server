package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.repository.RoleRepository;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.util.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByNameAndIsActive(name, EntityStatus.ACTIVE).orElseThrow(() -> new CustomException("Role not found"));
    }
}
