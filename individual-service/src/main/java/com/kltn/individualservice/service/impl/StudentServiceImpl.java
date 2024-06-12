package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.repository.StudentRepository;
import com.kltn.individualservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents(List<StudentStatus> studentStatuses) {
        return studentRepository.findByIsActiveInAndStatusIn(List.of(EntityStatus.ACTIVE), studentStatuses);
    }
}
