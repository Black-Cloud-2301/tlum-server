package com.kltn.individualservice.config._init.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.individualservice.dto.request.MajorImport;
import com.kltn.individualservice.dto.request.SubjectImport;
import com.kltn.individualservice.entity.*;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataImporter extends com.kltn.individualservice.config._init.DataImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataImporter.class);
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final StudyDepartmentRepository studyDepartmentRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;

    @Value("classpath:static/user.json")
    private Resource usersData;
    @Value("classpath:static/roles.json")
    private Resource rolesData;
    @Value("classpath:static/permissions.json")
    private Resource permissionsData;
    @Value("classpath:static/studyDepartments.json")
    private Resource studyDepartmentsData;
    @Value("classpath:static/majors.json")
    private Resource majorsData;
    @Value("classpath:static/subjects.json")
    private Resource subjectsData;


    @Override
    public void importData() {
        if (permissionRepository.count() == 0) {
            try {
                URL url = permissionsData.getURL();
                List<Permission> permissionsTemp = objectMapper.readValue(url, new TypeReference<>() {
                });
                ArrayList<Permission> permissions = new ArrayList<>();
                permissionsTemp.forEach(permission -> {
                    String[] actions = permission.getAction().split(",");
                    for (String action : actions) {
                        Permission newPermission = new Permission();
                        newPermission.setModule(permission.getModule());
                        newPermission.setFunction(permission.getFunction());
                        newPermission.setAction(action);
                        permissions.add(newPermission);
                    }
                });
                permissionRepository.saveAll(permissions);
            } catch (IOException e) {
                LOGGER.error("Failed to import permissions data", e);
            }
        }
        if (roleRepository.count() == 0) {
            try {
                URL url = rolesData.getURL();
                List<Role> roles = objectMapper.readValue(url, new TypeReference<>() {
                });
                roles.forEach(role -> {
                    switch (role.getCode()) {
                        case "SYS_ADMIN" -> role.setPermissions(permissionRepository.findAll());
                        case "STUDENT" -> {
                            List<Permission> permissions = permissionRepository.findByModuleInAndFunctionInAndActionIn(List.of("INDIVIDUAL"), List.of("STUDENT"), List.of("VIEW"));
                            role.setPermissions(permissions);
                        }
                        case "TEACHER" -> {
                            List<Permission> permissions = permissionRepository.findByModuleInAndFunctionInAndActionIn(List.of("INDIVIDUAL"), List.of("TEACHER"), List.of("VIEW"));
                            role.setPermissions(permissions);
                        }
                        case "EMPLOYEE" -> {
                            List<Permission> permissions = permissionRepository.findByModuleInAndFunctionInAndActionIn(List.of("INDIVIDUAL"), List.of("EMPLOYEE"), List.of("VIEW"));
                            role.setPermissions(permissions);
                        }
                    }
                });
                roleRepository.saveAll(roles);
            } catch (IOException e) {
                LOGGER.error("Failed to import roles data", e);
            }
        }
        if (userRepository.count() == 0) {
            try {
                URL url = usersData.getURL();
                List<User> users = objectMapper.readValue(url, new TypeReference<>() {
                });
                users.forEach(user -> {
                    user.setPassword(passwordEncoder.encode("123456aA@"));
                    Set<Role> rolesSet = new HashSet<>(roleRepository.findAll());
                    user.setRoles(rolesSet);
                });
                userRepository.saveAll(users);
            } catch (IOException e) {
                LOGGER.error("Failed to import user data", e);
            }
        }
        if (studyDepartmentRepository.count() == 0) {
            try {
                URL url = studyDepartmentsData.getURL();
                List<StudyDepartment> studyDepartments = objectMapper.readValue(url, new TypeReference<>() {
                });
                studyDepartmentRepository.saveAll(studyDepartments);
            } catch (IOException e) {
                LOGGER.error("Failed to import study department data", e);
            }
        }
        if (majorRepository.count() == 0) {
            try {
                URL url = majorsData.getURL();
                List<MajorImport> majorsDto = objectMapper.readValue(url, new TypeReference<>() {
                });
                List<Major> majorList = new ArrayList<>();
                majorsDto.forEach(majorDto -> {
                    Major major = new Major();
                    major.setCode(majorDto.getCode());
                    major.setName(majorDto.getName());
                    major.setStudyDepartment(studyDepartmentRepository.findByCode(majorDto.getStudyDepartmentCode()).orElseThrow(() -> new NotFoundException("Study Department")));
                    majorList.add(major);
                });
                majorRepository.saveAll(majorList);
            } catch (IOException e) {
                LOGGER.error("Failed to import major data", e);
            }
        }
        if (subjectRepository.count() == 0) {
            try {
                URL url = subjectsData.getURL();
                List<SubjectImport> subjectsDto = objectMapper.readValue(url, new TypeReference<>() {
                });
                subjectsDto.forEach(subjectDto -> {
                    Subject subject = new Subject();
                    subject.setCode(subjectDto.getCode());
                    subject.setName(subjectDto.getName());
                    subject.setCredit(subjectDto.getCredit());
                    subject.setHours(subjectDto.getHours());
                    subject.setCoefficient(subjectDto.getCoefficient());
                    subject.setRequireSubjects(subjectRepository.findAllByCodeIn(subjectDto.getRequireSubjects()));
                    subjectRepository.save(subject);
                });
            } catch (IOException e) {
                LOGGER.error("Failed to import subject data", e);
            }
        }
    }
}