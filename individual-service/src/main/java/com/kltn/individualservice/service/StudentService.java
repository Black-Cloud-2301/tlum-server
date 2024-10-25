package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.GetStudentsRequest;
import com.kltn.individualservice.dto.request.StudentRequestCRU;
import com.kltn.individualservice.dto.response.MyStudyInfoResponse;
import com.kltn.individualservice.dto.response.StudentOptions;
import com.kltn.individualservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    List<Student> getStudents(GetStudentsRequest request);
    List<Student> getStudentsNotRegister(GetStudentsRequest request);
    List<Student> getStudentsByStudyClass(Long studyClassId);
    Page<Student> getStudents(GetStudentsRequest request, Pageable pageable);
    Student findById(Long id);
    Student createStudent(StudentRequestCRU request);
    List<Student> importStudents(MultipartFile file);
    MyStudyInfoResponse getMyInfo(Long userId);
    List<StudentOptions> getStudentOptions(GetStudentsRequest request);
}
