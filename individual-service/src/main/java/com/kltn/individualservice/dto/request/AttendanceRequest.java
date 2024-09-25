package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AttendanceRequest {
    private Long studyClassId;
    private List<AttendanceDetailRequest> details;
}
