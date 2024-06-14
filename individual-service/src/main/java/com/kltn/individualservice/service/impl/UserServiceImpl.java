package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.ActionPermission;
import com.kltn.individualservice.annotation.FunctionPermission;
import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.UserRepository;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.exception.converter.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FunctionPermission("AUTH/USER")
public class UserServiceImpl implements UserService {
    @Value("${individual.defaultPassword}")
    private String defaultPassword;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final FileServiceClient fileServiceClient;
    private final HttpServletRequest request;
    private final StudentService studentService;

    @Override
    @ActionPermission("CREATE")
    public List<User> importUsers(MultipartFile file, UserType type) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(header, file);
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

        List<User> savedUsers = userRepository.saveAll(users);
        if (type == UserType.STUDENT) {
            studentService.saveStudentByUser(savedUsers);
        }
        return savedUsers;
    }

    @Override
    @ActionPermission("UPDATE")
    public User updateUser(UserRequestCRU userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.user.id")));

        user.setCode(userDto.getCode().toUpperCase());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setAvatar(userDto.getAvatar());
        user.setGender(userDto.getGender());
        user.setDateOfBirth(userDto.getDateOfBirth());

        return userRepository.save(user);
    }

    private void validateUserImport(List<Object> rowData) {
        if (rowData.getFirst() == null || rowData.getFirst().toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.field.user.code"));
        }
    }
}
