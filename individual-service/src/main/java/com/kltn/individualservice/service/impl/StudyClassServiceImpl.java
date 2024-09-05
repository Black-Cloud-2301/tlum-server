package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassCRU;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.entity.Subject;
import com.kltn.individualservice.entity.Teacher;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.StudyClassRepository;
import com.kltn.individualservice.service.StudyClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyClassServiceImpl implements StudyClassService {
    private final StudyClassRepository studyClassRepository;

    @Override
    public StudyClass create(StudyClassCRU studyClassCRU) {
        return studyClassRepository.save(createOrUpdateStudyClass(new StudyClass(), studyClassCRU));
    }

    @Override
    public StudyClass update(StudyClassCRU studyClassCRU) {
        StudyClass studyClass = studyClassRepository.findById(studyClassCRU.getId()).orElseThrow(() -> new NotFoundException("Study class"));
        return studyClassRepository.save(createOrUpdateStudyClass(studyClass, studyClassCRU));
    }

    @Override
    public StudyClass findById(Long id) {
        return studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
    }

    @Override
    public StudyClass delete(Long id) {
        StudyClass studyClass = studyClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Study class"));
        studyClass.setIsActive(EntityStatus.DELETED);
        return studyClassRepository.save(studyClass);
    }

    private StudyClass createOrUpdateStudyClass(StudyClass studyClass, StudyClassCRU studyClassCRU) {
        studyClass.setName(studyClassCRU.getName());
        studyClass.setSemester(studyClassCRU.getSemester());
        studyClass.setStudentGroup(studyClassCRU.getStudentGroup());
        studyClass.setYear(studyClassCRU.getYear());
        studyClass.setClassesOfWeek(studyClassCRU.getClassesOfWeek());
        studyClass.setTeacher(new Teacher(studyClassCRU.getTeacherId()));
        studyClass.setTotalStudent(studyClassCRU.getTotalStudent());
        studyClass.setSubject(new Subject(studyClassCRU.getSubjectId()));

        return studyClass;
    }

    @Override
    public List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses) {
        return studyClassRepository.findAllByIsActiveIn(statuses);
    }

    @Override
    public Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable) {
        return studyClassRepository.findAllByIsActiveIn(request, pageable);
    }
}
