package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.annotation.ActionPermission;
import com.kltn.individualservice.annotation.FunctionPermission;
import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.StudentRequestCRU;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.service.MajorService;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FunctionPermission("INDIVIDUAL/STUDENT")
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final MajorService majorService;

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
        return studentRepository.findById(id).orElseThrow(()-> new NotFoundException(I18n.getMessage("msg.field.student")));
    }

    @ActionPermission("CREATE")
    public Student createStudent(StudentRequestCRU request) {
        Student student = new Student();
        student.setMajor(majorService.getMajor(request.getMajorId()));
        student.setUser(new User(request));
        return studentRepository.save(student);
    }

    @ActionPermission("IMPORT")
    public List<Student> importStudents(MultipartFile file) {
        List<User> users = userService.importUsers(file, UserType.STUDENT);
        List<Student> students = users.stream().map(Student::new).toList();
        return studentRepository.saveAll(students);
    }

}
