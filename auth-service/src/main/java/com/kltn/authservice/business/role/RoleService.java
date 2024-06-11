package com.kltn.authservice.business.role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role getRole(String id);
    Role getRoleByName(String name);
    List<Role> getAllRoles();
    Set<String> flattenRoles(Set<Role> roles);
}
