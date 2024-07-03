package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.ActionPermission;
import com.kltn.individualservice.annotation.FunctionPermission;
import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.StudentRequestCRU;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.service.MajorService;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.StudentService;
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
@FunctionPermission("INDIVIDUAL/STUDENT")
public class StudentServiceImpl implements StudentService {
    @Value("${individual.defaultPassword}")
    private String defaultPassword;

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final MajorService majorService;
    private final HttpServletRequest request;
    private final FileServiceClient fileServiceClient;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @ActionPermission("VIEW")
    public List<Student> getStudents(GetStudentsRequest request) {
        return studentRepository.findByIsActiveInAndStatusIn(request.getEntityStatuses(), request.getStudentStatuses());
    }

    @ActionPermission("VIEW")
    public Page<Student> getStudents(GetStudentsRequest request, Pageable pageable) {
        return studentRepository.findByIsActiveInAndStatusIn(request.getEntityStatuses(), request.getStudentStatuses(), pageable);
    }

    @ActionPermission("VIEW")
    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.field.student")));
    }

    @ActionPermission("CREATE")
    public Student createStudent(StudentRequestCRU request) {
        Student student = new Student();
        student.setMajors(majorService.getMajor(request.getMajorId()));
        student.setUser(new User(request));
        return studentRepository.save(student);
    }

    @Override
    @ActionPermission("IMPORT")
    public List<Student> importStudents(MultipartFile file) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(header, file);
        Role defaultRole = roleService.getRoleByCode(UserType.STUDENT.name());
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        AtomicInteger currentMaxCode = new AtomicInteger(userService.findMaxNumberInCode(UserType.STUDENT));
        List<Student> students = new ArrayList<>();
        for (List<Object> rowData : excelData) {
            this.validateStudentImport(rowData);
            Student student = new Student();
            student.setStatus(StudentStatus.REGISTERED);

            student.setMajors(majorService.getMajorByCodeAndIsActive((String) rowData.get(8), EntityStatus.ACTIVE));
            String codeSuffix = String.format("%05d", currentMaxCode.incrementAndGet());

            User user = new User();
            user.setCode(Optional.ofNullable((String) rowData.get(0)).orElseGet(() -> "A" + codeSuffix));
            user.setFirstname((String) rowData.get(1));
            user.setLastname((String) rowData.get(2));
            user.setPhoneNumber((String) rowData.get(3));
            user.setEmail((String) rowData.get(4));
            user.setGender(Gender.fromString((String) rowData.get(5)));
            user.setDateOfBirth(DateConverter.convertImport((String) rowData.get(6)));
            user.setAddress((String) rowData.get(7));
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setRoles(roles);
            student.setUser(user);
            students.add(student);
        }

        return studentRepository.saveAll(students);
    }


    private void validateStudentImport(List<Object> rowData) {
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