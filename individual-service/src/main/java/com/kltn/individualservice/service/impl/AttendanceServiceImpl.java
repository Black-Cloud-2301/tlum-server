//package com.kltn.individualservice.service.impl;
//
//import com.kltn.individualservice.dto.request.AttendanceDetailRequest;
//import com.kltn.individualservice.dto.request.AttendanceRequest;
//import com.kltn.individualservice.entity.Attendance;
//import com.kltn.individualservice.entity.StudentStudyClass;
//import com.kltn.individualservice.exception.NotFoundException;
//import com.kltn.individualservice.repository.AttendanceRepository;
//import com.kltn.individualservice.repository.StudentStudyClassRepository;
//import com.kltn.individualservice.service.AttendanceService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AttendanceServiceImpl implements AttendanceService {
//    AttendanceRepository attendanceRepository;
//    StudentStudyClassRepository studentStudyClassRepository;
//
//    @Override
//    public List<Attendance> findByStudyClass(Long studyClassId) {
//        return attendanceRepository.findByStudyClass(studyClassId);
//    }
//
//    @Override
//    public List<Attendance> save(AttendanceRequest requests) {
//        List<Attendance> attendances = new ArrayList<>();
//
//        Map<Long, StudentStudyClass> studentStudyClassMap = studentStudyClassRepository.findByStudyClassId(requests.getStudyClassId()).stream()
//                .collect(Collectors.toMap(studentStudyClass -> studentStudyClass.getStudent().getId(), studentStudyClass -> studentStudyClass));
//
//        for (AttendanceDetailRequest request : requests.getDetails()) {
//            StudentStudyClass studentStudyClass = studentStudyClassMap.get(request.getStudentId());
//            if (studentStudyClass == null) {
//                throw new NotFoundException("StudentStudyClass");
//            }
//
//            studentStudyClass.setMiddleScore(request.getMiddleScore());
//            studentStudyClass.setFinalScore(request.getFinalScore());
//
//            for (int week = 1; week <= 9; week++) {
//                Boolean attendanceValue = getAttendanceValue(request, week);
//                if (attendanceValue == null) {
//                    attendanceValue = false;
//                }
//
////                Attendance attendance = attendanceRepository.findByStudentStudyClassAndWeekNumber(studentStudyClass, week)
////                        .orElse(new Attendance());
////                attendance.setStudentStudyClass(studentStudyClass);
////                attendance.setWeekNumber(week);
////                attendance.setAttendance(attendanceValue);
////                attendances.add(attendance);
//            }
//        }
//        return attendanceRepository.saveAll(attendances);
//    }
//
//    private Boolean getAttendanceValue(AttendanceDetailRequest request, int week) {
//        return switch (week) {
//            case 1 -> request.getWeek1();
//            case 2 -> request.getWeek2();
//            case 3 -> request.getWeek3();
//            case 4 -> request.getWeek4();
//            case 5 -> request.getWeek5();
//            case 6 -> request.getWeek6();
//            case 7 -> request.getWeek7();
//            case 8 -> request.getWeek8();
//            case 9 -> request.getWeek9();
//            default -> null;
//        };
//    }
//}
