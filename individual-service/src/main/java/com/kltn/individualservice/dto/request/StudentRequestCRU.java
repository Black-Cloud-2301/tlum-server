package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.StudentStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentRequestCRU extends UserRequestCRU {
    private String code;
    private String password;
    private Long majorId;
    private StudentStatus studentStatus;
}
