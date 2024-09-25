package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SubjectsRequest;
import com.kltn.individualservice.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByIsActiveIn(List<EntityStatus> statuses);
    @Query("SELECT s " +
            "FROM Subject s " +
            "JOIN s.majors m " +
            "WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.code} IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (:#{#request.majorIds} IS NULL OR m.id IN :#{#request.majorIds}) " +
            "GROUP BY s.id")
    Page<Subject> findAllByIsActiveIn(SubjectsRequest request, Pageable pageable);

//    @Query("SELECT s FROM Subject s " +
//       "LEFT JOIN s.requireSubjects sr " +
//       "JOIN s.majors m " +
//       "JOIN Student st ON st.majors.id = m.id " +
//       "LEFT JOIN StudentStudyClass ss ON ss.studyClass.subject.id = sr.id " +
//       "LEFT JOIN StudentStudyClass st2 ON st2.studyClass.subject.id = s.id " +
//       "WHERE s.isActive = 1 " +
//       "AND st.id = :studentId " +
//       "AND (sr.id IS NULL OR (ss.isActive = 1 AND ss.student.id = :studentId)) " +
//       "AND (s.requireCredit IS NULL OR s.requireCredit <= " +
//       "(SELECT SUM(st2.studyClass.subject.credit) FROM StudentStudyClass st2 " +
//       "WHERE st2.isActive = 1 AND st2.studyClass.isActive = 1 AND st2.student.id = :studentId))")
//    List<Subject> findAllSubjectCanRegister(Long studentId);
}