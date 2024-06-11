package com.kltn.individualservice.service;

import com.kltn.individualservice.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);
    void checkFunctionPermission(List<String> roles, String function);
    void checkActionPermission(List<String> roles, String action, String function);
}
