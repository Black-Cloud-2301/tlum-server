package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.response.ReportCard;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentStudyClassRepository extends JpaRepository<StudentStudyClass, Long> {
    Optional<StudentStudyClass> findByStudentAndStudyClassAndIsActive(Student student, StudyClass studyClass, EntityStatus isActive);
    List<StudentStudyClass> findAllByStudentIdAndIsActive(Long studentId, EntityStatus isActive);

    @Query("SELECT ssc " +
           "FROM StudentStudyClass ssc " +
           "WHERE ssc.isActive = 1 " +
           "AND ssc.student.id = :studentId " +
           "AND ssc.studyClass.semester.id = :semesterId")
    List<StudentStudyClass> findAllByStudentId(Long studentId, Long semesterId);

    Optional<StudentStudyClass> findByIdAndIsActive(Long id, EntityStatus isActive);

    @Query("SELECT ssc " +
           "FROM StudentStudyClass ssc " +
           "WHERE ssc.isActive = 1 " +
           "AND ssc.studyClass.id = :studyClassId")
    List<StudentStudyClass> findByStudyClassId(Long studyClassId);

    @Query("SELECT ssc " +
           "FROM StudentStudyClass ssc " +
           "WHERE ssc.isActive = 1 " +
           "AND ssc.student.id = :studentId " +
           "AND (current_date BETWEEN ssc.studyClass.semester.fromDate AND ssc.studyClass.semester.toDate)")
    List<StudentStudyClass> findCurrentTimetable(Long studentId);

//    @Query("SELECT new com.kltn.individualservice.dto.response.ReportCard(s.name, COUNT(a.id), ssc.middleScore, ssc.finalScore) " +
//           "FROM StudentStudyClass ssc " +
//           "JOIN ssc.studyClass sc " +
//           "JOIN sc.subject s " +
//           "JOIN ssc.attendances a " +
//           "WHERE ssc.isActive = 1 " +
//           "AND ssc.student.id = :studentId " +
//           "AND sc.semester.id = :semesterId " +
//           "AND a.attendance = true " +
//           "GROUP BY ssc.id, s.name, ssc.middleScore, ssc.finalScore")
//    List<ReportCard> findReportCard(Long semesterId, Long studentId);
}