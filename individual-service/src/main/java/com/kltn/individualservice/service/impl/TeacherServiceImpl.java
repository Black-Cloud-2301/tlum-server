package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.GetTeachersRequest;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.Teacher;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.TeacherRepository;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.StudyDepartmentService;
import com.kltn.individualservice.service.TeacherService;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.exception.converter.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    @Value("${individual.defaultPassword}")
    private String defaultPassword;

    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final HttpServletRequest request;
    private final FileServiceClient fileServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final StudyDepartmentService studyDepartmentService;

    @Override
    public List<Teacher> getTeachers(GetTeachersRequest request) {
        return teacherRepository.findAllByIsActiveInAndStatusIn(request.getEntityStatuses(),request.getEmployeeStatuses());
    }

    @Override
    public Page<Teacher> getTeachers(GetTeachersRequest request, Pageable pageable) {
        return teacherRepository.findAllByIsActiveInAndStatusIn(request.getEntityStatuses(),request.getEmployeeStatuses(),pageable);
    }


    @Override
    public List<Teacher> importTeachers(MultipartFile file) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(header, file);
        Role defaultRole = roleService.getRoleByCode(UserType.TEACHER.name());
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        AtomicInteger currentMaxCode = new AtomicInteger(userService.findMaxNumberInCode(UserType.TEACHER));
        List<Teacher> teachers = new ArrayList<>();
        for (List<Object> rowData : excelData) {
            this.validateTeacherImport(rowData);
            Teacher teacher = new Teacher();
            teacher.setStatus(EmployeeStatus.PROBATION);

            teacher.setStudyDepartment(studyDepartmentService.getStudyDepartmentByCodeAndEntityStatus((String) rowData.get(8), EntityStatus.ACTIVE));
            String codeSuffix = String.format("%05d", currentMaxCode.incrementAndGet());

            User user = new User();
            user.setCode(Optional.ofNullable((String) rowData.get(0)).orElseGet(() -> "T" + codeSuffix));
            user.setFirstname((String) rowData.get(1));
            user.setLastname((String) rowData.get(2));
            user.setPhoneNumber((String) rowData.get(3));
            user.setEmail((String) rowData.get(4));
            user.setGender(Gender.fromString((String) rowData.get(5)));
            user.setDateOfBirth(DateConverter.convertImport((String) rowData.get(6)));
            user.setAddress((String) rowData.get(7));
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setRoles(roles);
            teacher.setUser(user);
            teachers.add(teacher);
        }

        return teacherRepository.saveAll(teachers);
    }

    private void validateTeacherImport(List<Object> rowData) {
        if (rowData.get(1) == null || rowData.get(1).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.firstname")));
        }
        if (rowData.get(2) == null || rowData.get(2).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.lastname")));
        }
        if (rowData.get(3) == null || rowData.get(3).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.phone")));
        }
        if (rowData.get(4) == null || rowData.get(4).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", "Email"));
        }
        if (rowData.get(5) == null || rowData.get(5).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.gender")));
        }
        if (rowData.get(6) == null || rowData.get(6).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.dateOfBirth")));
        }
    }
}
