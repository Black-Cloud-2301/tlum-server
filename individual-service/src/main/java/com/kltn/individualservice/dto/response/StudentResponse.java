package com.kltn.individualservice.dto.response;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.entity.Student;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Instant;

@Getter
public class StudentResponse {
    private final Long id;
    private final String code;
    private final String firstname;
    private final String lastname;
    private final String phoneNumber;
    private final Gender gender;
    private final LocalDate dateOfBirth;
    private final String email;
    private final String address;
    private final Long avatar;
    private final Instant createdAt;
    private final String createdBy;
    private final Instant lastUpdatedAt;
    private final String lastUpdatedBy;
    private final EntityStatus isActive;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.code = student.getUser().getCode();
        this.firstname = student.getUser().getFirstname();
        this.lastname = student.getUser().getLastname();
        this.phoneNumber = student.getUser().getPhoneNumber();
        this.gender = student.getUser().getGender();
        this.dateOfBirth = student.getUser().getDateOfBirth();
        this.email = student.getUser().getEmail();
        this.address = student.getUser().getAddress();
        this.avatar = student.getUser().getAvatar();
        this.createdAt = student.getCreatedAt();
        this.createdBy = student.getCreatedBy();
        this.lastUpdatedAt = student.getLastUpdatedAt();
        this.lastUpdatedBy = student.getLastUpdatedBy();
        this.isActive = student.getIsActive();
    }
}
