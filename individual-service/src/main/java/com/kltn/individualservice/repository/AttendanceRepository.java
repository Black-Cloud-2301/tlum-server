//package com.kltn.individualservice.repository;
//
//import com.kltn.individualservice.entity.Attendance;
//import com.kltn.individualservice.entity.StudentStudyClass;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
////    @Query("SELECT a FROM Attendance a WHERE a.studentStudyClass.studyClass.id = :studyClassId AND a.isActive = 1")
////    List<Attendance> findByStudyClass(Long studyClassId);
//
////    Optional<Attendance> findByStudentStudyClassAndWeekNumber(StudentStudyClass studentStudyClass, Integer weekNumber);
//}