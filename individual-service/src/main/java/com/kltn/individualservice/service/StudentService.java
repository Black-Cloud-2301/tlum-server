package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents(List<StudentStatus> request);
}
