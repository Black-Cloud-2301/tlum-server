package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {
    List<Subject> getSubjects(SubjectsRequest request);

    Subject createSubject(Subject request);
    Subject updateSubject(Subject request);

    Page<Subject> getSubjects(SubjectsRequest request, Pageable pageable);

    List<Subject> findAllById(List<Long> subjectIds);
    void deleteSubject(Long id);
}
