package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.SemesterRequest;
import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SemesterService {
    List<Semester> getSemesters(SemesterRequest request);
    Page<Semester> getSemesters(SemesterRequest request, Pageable pageable);
    Semester createSemester(Semester request);
    Semester updateSemester(Semester request);
    Semester findById(Long id);
    void deleteSemester(Long id);
}
