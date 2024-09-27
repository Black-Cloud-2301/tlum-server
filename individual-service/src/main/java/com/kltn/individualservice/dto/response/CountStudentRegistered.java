package com.kltn.individualservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountStudentRegistered {
    private Long studyClassId;
    private Long registeredStudent;
}
