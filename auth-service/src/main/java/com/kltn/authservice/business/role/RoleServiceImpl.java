package com.kltn.authservice.business.role;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
       @Override
    public Set<String> flattenRoles(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getModule() + "/" + permission.getFunction() + "/" + permission.getAction())
                .collect(Collectors.toSet());
    }
}
