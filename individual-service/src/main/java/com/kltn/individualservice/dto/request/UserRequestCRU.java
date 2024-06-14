package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserRequestCRU {
    private Long id;
    private String code;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String address;
    private String avatar;
}
