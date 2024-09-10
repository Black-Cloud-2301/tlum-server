package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.GetTeachersRequest;
import com.kltn.individualservice.dto.request.TeacherRequest;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.entity.Teacher;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.TeacherRepository;
import com.kltn.individualservice.service.*;
import com.kltn.individualservice.util.exception.converter.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final SubjectService subjectService;

    @Override
    @Cacheable(value = "teachers", key = "#request")
    public List<Teacher> getTeachers(GetTeachersRequest request) {
        return teacherRepository.findAllByIsActiveInAndStatusIn(request);
    }

    @Override
    public Page<Teacher> getTeachers(GetTeachersRequest request, Pageable pageable) {
        return teacherRepository.findAllByIsActiveInAndStatusIn(request, pageable);
    }


    @Override
    @CacheEvict(value = "teachers", allEntries = true)
    @Transactional
    public List<Teacher> importTeachers(MultipartFile file) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(header, file);
        Role defaultRole = roleService.getRoleByCode(UserType.TEACHER.name());
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        AtomicInteger currentMaxCode = new AtomicInteger(userService.findMaxNumberInCode(UserType.TEACHER));
        List<Teacher> teachers = Collections.synchronizedList(new ArrayList<>());
        excelData.parallelStream().forEach(rowData -> {
            this.validateTeacherImport(rowData);
            Teacher teacher = new Teacher();
            teacher.setStatus(EmployeeStatus.PROBATION);

            teacher.setStudyDepartment(studyDepartmentService.getStudyDepartmentByCodeAndEntityStatus((String) rowData.get(8), EntityStatus.ACTIVE));
            String codeSuffix = String.format("%05d", currentMaxCode.incrementAndGet());

            User user = new User();
            String code = (String) rowData.getFirst();
            if (code == null || code.isEmpty()) {
                code = "T" + codeSuffix;
            }
            user.setCode(code);
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
        });

        return teacherRepository.saveAll(teachers);
    }

    @Override
    @CacheEvict(value = "teachers", allEntries = true)
    public Teacher updateTeacher(TeacherRequest request) {
        Teacher teacherEntity = teacherRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Teacher"));
        teacherEntity.setStatus(request.getEmployeeStatus());
        teacherEntity.setStudyDepartment(studyDepartmentService.findById(request.getStudyDepartmentId()));

        User user = teacherEntity.getUser();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setAddress(request.getAddress());

        List<Subject> subjects = subjectService.findAllById(request.getSubjectIds());
        teacherEntity.setSubjects(subjects);
        return teacherRepository.save(teacherEntity);
    }

    @Override
    @CacheEvict(value = "teachers", allEntries = true)
    public Teacher deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher"));
        teacher.setIsActive(EntityStatus.DELETED);
        teacher.getUser().setIsActive(EntityStatus.DELETED);
        return teacherRepository.save(teacher);
    }

    @Override
    @CacheEvict(value = "teachers", allEntries = true)
    public Teacher createTeacher(TeacherRequest request) {
        Teacher teacher = new Teacher();
        User user = new User();
        teacher.setStatus(request.getEmployeeStatus());
        teacher.setStudyDepartment(studyDepartmentService.findById(request.getStudyDepartmentId()));
        AtomicInteger currentMaxCode = new AtomicInteger(userService.findMaxNumberInCode(UserType.TEACHER));
        String codeSuffix = String.format("%05d", currentMaxCode.incrementAndGet());

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setAddress(request.getAddress());
        user.setCode("T" + codeSuffix);
        teacher.setUser(user);

        List<Subject> subjects = subjectService.findAllById(request.getSubjectIds());
        teacher.setSubjects(subjects);
        return teacherRepository.save(teacher);
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
