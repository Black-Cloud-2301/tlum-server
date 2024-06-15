package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
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
    private String avatarUrl;
    private MultipartFile avatarUpload;
}
