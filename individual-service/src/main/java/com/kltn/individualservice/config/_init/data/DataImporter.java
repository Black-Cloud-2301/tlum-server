package com.kltn.individualservice.config._init.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.MajorImport;
import com.kltn.individualservice.dto.request.SubjectImport;
import com.kltn.individualservice.dto.request.SubjectMajorImport;
import com.kltn.individualservice.entity.*;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataImporter extends com.kltn.individualservice.config._init.DataImporter {

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final StudyDepartmentRepository studyDepartmentRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;
    private final SettingRepository settingRepository;

    @Value("classpath:static/user.json")
    private Resource usersData;
    @Value("classpath:static/roles.json")
    private Resource rolesData;
    @Value("classpath:static/permissions.json")
    private Resource permissionsData;
    @Value("classpath:static/roles-permissions.json")
    private Resource rolesPermissionsData;
    @Value("classpath:static/studyDepartments.json")
    private Resource studyDepartmentsData;
    @Value("classpath:static/majors.json")
    private Resource majorsData;
    @Value("classpath:static/subjects.json")
    private Resource subjectsData;
    @Value("classpath:static/majorsSubject.json")
    private Resource majorsSubjectData;
    @Value("classpath:static/settings.json")
    private Resource settingsData;


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
                log.error("Failed to import permissions data", e);
            }
        }
        if (roleRepository.count() == 0) {
            try {
                URL url = rolesData.getURL();
                List<Role> roles = objectMapper.readValue(url, new TypeReference<>() {
                });
                List<Map<String, Object>> rolesPermissions = objectMapper.readValue(rolesPermissionsData.getURL(), new TypeReference<>() {
                });

                roles.forEach(role -> {
                    if ("SYS_ADMIN".equals(role.getCode())) {
                        role.setPermissions(permissionRepository.findAll());
                    } else {
                        rolesPermissions.forEach(rolePermission -> {
                            if (role.getCode().equals(rolePermission.get("code"))) {
                                List<Map<String, String>> permissionsList = (List<Map<String, String>>) rolePermission.get("permission");
                                List<Permission> permissions = new ArrayList<>();
                                permissionsList.forEach(permissionMap -> {
                                    String module = permissionMap.get("module");
                                    String function = permissionMap.get("function");
                                    String[] actions = permissionMap.get("action").split(",");
                                    for (String action : actions) {
                                        Permission permission = permissionRepository.findByModuleAndFunctionAndAction(module, function, action).orElseThrow(() -> new NotFoundException("Permission"));
                                        if (permission != null) {
                                            permissions.add(permission);
                                        }
                                    }
                                });
                                role.setPermissions(permissions);
                            }
                        });
                    }
                });
                roleRepository.saveAll(roles);
            } catch (IOException e) {
                log.error("Failed to import roles data", e);
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
                log.error("Failed to import user data", e);
            }
        }
        if (studyDepartmentRepository.count() == 0) {
            try {
                URL url = studyDepartmentsData.getURL();
                List<StudyDepartment> studyDepartments = objectMapper.readValue(url, new TypeReference<>() {
                });
                studyDepartmentRepository.saveAll(studyDepartments);
            } catch (IOException e) {
                log.error("Failed to import study department data", e);
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
                log.error("Failed to import major data", e);
            }
        }
        if (subjectRepository.count() == 0) {
            try {
                URL majorSubjectUrl = majorsSubjectData.getURL();
                List<SubjectMajorImport> subjectsMajorDto = objectMapper.readValue(majorSubjectUrl, new TypeReference<>() {
                });
                Map<String, Major> majors = majorRepository.findByIsActiveIn(List.of(EntityStatus.ACTIVE)).stream()
                        .collect(Collectors.toMap(Major::getCode, major -> major));

                URL subjectUrl = subjectsData.getURL();
                List<SubjectImport> subjectsDto = objectMapper.readValue(subjectUrl, new TypeReference<>() {
                });
                List<Subject> subjects = new ArrayList<>();

                // Step 1: Save subjects without requireSubjects
                subjectsDto.forEach(subjectDto -> {
                    Subject subject = new Subject();
                    subject.setCode(subjectDto.getCode());
                    subject.setName(subjectDto.getName());
                    subject.setCredit(subjectDto.getCredit());
                    subject.setHours(subjectDto.getHours());
                    subject.setCoefficient(subjectDto.getCoefficient());
                    subject.setRequireCredit(subjectDto.getRequireCredit());
                    List<Major> subjectMajors = subjectsMajorDto.stream()
                            .filter(subjectMajorDto -> subjectMajorDto.getSubjectCode().equals(subjectDto.getCode()))
                            .map(subjectMajorDto -> majors.get(subjectMajorDto.getMajorCode()))
                            .collect(Collectors.toList());
                    subject.setMajors(subjectMajors);
                    subjects.add(subject);
                });
                List<Subject> subjectsSaved = subjectRepository.saveAll(subjects);

                // Step 2: Update subjects with requireSubjects
                subjectsSaved.forEach(subject -> {
                    List<String> requireSubjectCodes = subjectsDto.stream()
                            .filter(subjectDto -> subjectDto.getCode().equals(subject.getCode()))
                            .findFirst()
                            .map(SubjectImport::getRequireSubjects)
                            .orElse(Collections.emptyList());

                    List<Subject> requireSubjects = subjectsSaved.stream()
                            .filter(savedSubject -> requireSubjectCodes.contains(savedSubject.getCode()))
                            .collect(Collectors.toList());

                    subject.setRequireSubjects(requireSubjects);
                });
                subjectRepository.saveAll(subjectsSaved);
            } catch (IOException e) {
                log.error("Failed to import subject data", e);
            }
        }
        if (settingRepository.count() == 0) {
            try {
                URL url = settingsData.getURL();
                List<Setting> settings = objectMapper.readValue(url, new TypeReference<>() {
                });
                settingRepository.saveAll(settings);
            } catch (IOException e) {
                log.error("Failed to import setting data", e);
            }
        }
    }
}