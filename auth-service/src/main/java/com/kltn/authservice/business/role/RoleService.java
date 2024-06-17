package com.kltn.authservice.business.role;

import java.util.Set;

public interface RoleService {

    Set<String> flattenRoles(Set<Role> roles);
}
