package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.exception.NoPermissionException;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.RoleRepository;
import com.kltn.individualservice.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByNameAndIsActive(name, EntityStatus.ACTIVE).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.role.title")));
    }

    @Override
    public void checkFunctionPermission(List<String> roles, String function) {
        roles.stream()
                .map(role -> role.split("/"))
                .map(parts -> parts[0] + "/" + parts[1])
                .filter(role -> role.equals(function))
                .findFirst()
                .orElseThrow(NoPermissionException::new);
    }

    @Override
    public void checkActionPermission(List<String> roles, String action, String function) {
        String[] actions = action.split("&");
        roles.stream()
                .map(role -> role.split("/"))
                .filter(parts -> parts.length == 3 && (parts[0] + '/' + parts[1]).equals(function) && Arrays.asList(actions).contains(parts[2]))
                .findFirst()
                .orElseThrow(NoPermissionException::new);
    }
}
