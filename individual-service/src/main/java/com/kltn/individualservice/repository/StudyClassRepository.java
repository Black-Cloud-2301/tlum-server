package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.StudyClassRequest;
import com.kltn.individualservice.dto.response.CountStudentRegistered;
import com.kltn.individualservice.entity.Semester;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyClassRepository extends JpaRepository<StudyClass, Long> {
    List<StudyClass> findAllByIsActiveIn(List<EntityStatus> statuses);

    @Query("SELECT s " +
           "FROM StudyClass s " +
           "WHERE s.isActive IN :#{#request.entityStatuses} " +
           "AND (:#{#request.name} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
           "AND (:#{#request.year} IS NULL OR s.semester.year = :#{#request.year}) " +
           "AND (:#{#request.studentGroup} IS NULL OR s.semester.studentGroup = :#{#request.studentGroup}) " +
           "AND (:#{#request.semester} IS NULL OR s.semester.semester = :#{#request.semester}) " +
           "AND (:#{#request.subjectId} IS NULL OR s.subject.id = :#{#request.subjectId}) " +
           "AND (:#{#request.teacherId} IS NULL OR s.teacher.id = :#{#request.teacherId}) " +
           "ORDER BY s.semester.year DESC, s.semester.studentGroup DESC, s.semester.semester DESC")
    Page<StudyClass> findAllByIsActiveIn(StudyClassRequest request, Pageable pageable);

    Optional<StudyClass> findByIdAndIsActive(Long id, EntityStatus isActive);

    @Query("SELECT s FROM StudyClass s " +
           "WHERE s.isActive = 1 " +
           "AND s.semester.id = :semesterId " +
           "AND s.subject.id IN " +
           "(SELECT s.id FROM Subject s LEFT JOIN s.requireSubjects sr " +
           "WHERE s.isActive = 1 AND (sr.id IS NULL OR sr.id IN " +
           "(SELECT ss.studyClass.subject.id FROM StudentStudyClass ss WHERE ss.isActive = 1 AND ss.student.id = :studentId AND ss.finalScore > 0))) " +
           "AND (s.subject.requireCredit IS NULL OR s.subject.requireCredit <= (SELECT SUM(st2.studyClass.subject.credit) FROM StudentStudyClass st2 " +
           "WHERE st2.isActive = 1 AND st2.studyClass.isActive = 1 AND st2.student.id = :studentId))")
    List<StudyClass> findStudyClassByStudentAndSemester(Long studentId, Long semesterId);

    @Query("SELECT new com.kltn.individualservice.dto.response.CountStudentRegistered(s.id, COUNT(ss.id)) " +
           "FROM StudyClass s LEFT JOIN StudentStudyClass ss ON s.id = ss.studyClass.id " +
           "WHERE s.isActive = 1 AND ss.isActive = 1 AND s.id IN :studyClassIds " +
           "GROUP BY s.id")
    List<CountStudentRegistered> countStudentRegistered(List<Long> studyClassIds);

    @Query("SELECT sc " +
           "FROM StudentStudyClass ssc " +
           "JOIN StudyClass sc ON ssc.studyClass.id = sc.id " +
           "WHERE ssc.isActive = 1  AND sc.isActive = 1 " +
           "GROUP BY sc.id, sc.totalStudent " +
           "HAVING COUNT(sc.id) >= sc.totalStudent")
    List<StudyClass> findStudyClassFullStudent(Long semesterId);
    
    @Query("SELECT s " +
           "FROM StudyClass s " +
           "WHERE s.isActive = 1 " +
           "AND s.teacher.id = :teacherId " +
           "AND (:#{#semester.fromDate} BETWEEN s.semester.fromDate AND s.semester.toDate " +
           "OR :#{#semester.toDate} BETWEEN s.semester.fromDate AND s.semester.toDate)")
    List<StudyClass> findStudyClassesCrossSemester(Semester semester, Long teacherId);

    @Query("SELECT sc FROM StudyClass sc JOIN sc.semester s WHERE sc.teacher.id = :teacherId AND (s.fromDate BETWEEN :startDate AND :endDate OR s.toDate BETWEEN :startDate AND :endDate OR :startDate BETWEEN s.fromDate AND s.toDate OR :endDate BETWEEN s.fromDate AND s.toDate)")
    List<StudyClass> findStudyClassesForCurrentWeek(Long teacherId, LocalDate startDate, LocalDate endDate);
}