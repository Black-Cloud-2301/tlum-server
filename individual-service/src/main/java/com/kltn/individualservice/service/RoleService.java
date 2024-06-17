package com.kltn.individualservice.service;

import com.kltn.individualservice.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByCode(String code);
    void checkFunctionPermission(List<String> roles, String function);
    void checkActionPermission(List<String> roles, String action, String function);
}
