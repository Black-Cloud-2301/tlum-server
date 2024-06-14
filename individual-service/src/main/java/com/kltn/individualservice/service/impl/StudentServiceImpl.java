package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents(GetStudentsRequest request) {
        return studentRepository.findByIsActiveInAndStatusIn(request.getEntityStatuses(), request.getStudentStatuses());
    }

    @Override
    public Page<Student> getStudents(GetStudentsRequest request, Pageable pageable) {
        return studentRepository.findByIsActiveInAndStatusIn(request.getEntityStatuses(), request.getStudentStatuses(), pageable);
    }

    @Override
    public void saveStudentByUser(List<User> users) {
        List<Student> students = users.stream()
                .map(Student::new)
                .toList();
        studentRepository.saveAll(students);
    }

}
