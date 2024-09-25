package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetStudentStudyClassesRequest;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudentStudyClass;
import com.kltn.individualservice.entity.StudyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentStudyClassRepository extends JpaRepository<StudentStudyClass, Long> {
  Optional<StudentStudyClass> findByStudentAndStudyClassAndIsActive(Student student, StudyClass studyClass, EntityStatus isActive);
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
}