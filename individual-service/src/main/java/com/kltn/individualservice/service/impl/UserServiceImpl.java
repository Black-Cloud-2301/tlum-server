package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.UserRepository;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.exception.CustomException;
import com.kltn.individualservice.util.exception.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Value("${individual.defaultPassword}")
    private String defaultPassword;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final FileServiceClient fileServiceClient;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, FileServiceClient fileServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.fileServiceClient = fileServiceClient;
    }

    @Override
    public List<User> importUsers(MultipartFile file, UserType type) {
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(file);
        List<User> users = new ArrayList<>();
        Role defaultRole = roleService.getRoleByName(type.name());
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        for (List<Object> rowData : excelData) {
            this.validateUserImport(rowData);
            User user = new User();
            user.setCode((String) rowData.get(0));
            user.setFirstname((String) rowData.get(1));
            user.setLastname((String) rowData.get(2));
            user.setPhoneNumber((String) rowData.get(3));
            user.setEmail((String) rowData.get(4));
            user.setGender(Gender.fromString((String) rowData.get(5)));
            user.setDateOfBirth(DateConverter.convertImport((String) rowData.get(6)));
            user.setAddress((String) rowData.get(7));
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setRoles(roles);
            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    private void validateUserImport(List<Object> rowData) {
        if (rowData.getFirst() == null || rowData.getFirst().toString().isEmpty()) {
            throw new CustomException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.code")));
        }

        if (rowData.get(0).toString().length() != 6) {
            throw new IllegalArgumentException("Code should not exceed 6 characters");
        }

        if (rowData.get(1) == null || rowData.get(1).toString().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (rowData.get(1).toString().length() > 255) {
            throw new IllegalArgumentException("First name should not exceed 255 characters");
        }

        if (rowData.get(2) == null || rowData.get(2).toString().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (rowData.get(2).toString().length() > 255) {
            throw new IllegalArgumentException("First name should not exceed 255 characters");
        }

        if (rowData.get(3) == null || rowData.get(3).toString().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        if (rowData.get(3).toString().length() > 12) {
            throw new IllegalArgumentException("Phone number should not exceed 12 characters");
        }

        if (rowData.get(4) == null || rowData.get(4).toString().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (rowData.get(4).toString().length() > 1023) {
            throw new IllegalArgumentException("Email should not exceed 1023 characters");
        }

        if (rowData.get(5) == null || rowData.get(5).toString().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }

        if (rowData.get(6) == null || rowData.get(6).toString().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (rowData.get(7) != null && rowData.get(7).toString().length() > 2047) {
            throw new IllegalArgumentException("Address should not exceed 2047 characters");
        }
    }
}
