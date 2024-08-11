package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EmployeeStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TeacherRequest {
    private Long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private Long studyDepartmentId;
    private List<Long> subjectIds;
    private EmployeeStatus employeeStatus;
}
