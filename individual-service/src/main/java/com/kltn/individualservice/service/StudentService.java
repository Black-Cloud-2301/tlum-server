package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    List<Student> getStudents(GetStudentsRequest request);
    Page<Student> getStudents(GetStudentsRequest request, Pageable pageable);

    void saveStudentByUser(List<User> users);
}
