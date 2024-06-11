package com.kltn.authservice.business.role;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.exceptions.NotFoundException;
import com.kltn.authservice.utils.enums.EntityStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    String serviceMessage = I18n.getMessage("msg.role.title");

    @Override
    public Role getRole(String id) {
        return roleRepository.findById(Long.parseLong(id)).orElseThrow(() -> new NotFoundException(serviceMessage));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByNameAndIsActive(name, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(serviceMessage));
    }

    @Override
    public List<Role> getAllRoles() {
        return List.of();
    }

    @Override
    public Set<String> flattenRoles(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getModule() + "/" + permission.getFunction() + "/" + permission.getAction())
                .collect(Collectors.toSet());
    }
}
