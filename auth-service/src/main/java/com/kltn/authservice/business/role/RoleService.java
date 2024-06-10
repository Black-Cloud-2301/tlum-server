package com.kltn.authservice.business.role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role getRole(String id);
    Role getRoleByName(String name);
    List<Role> getAllRoles();
}
