package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetTeachersRequest;
import com.kltn.individualservice.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    List<Teacher> getTeachers(GetTeachersRequest request);
    Page<Teacher> getTeachers(GetTeachersRequest request, Pageable pageable);
    List<Teacher> importTeachers(MultipartFile file);
}
