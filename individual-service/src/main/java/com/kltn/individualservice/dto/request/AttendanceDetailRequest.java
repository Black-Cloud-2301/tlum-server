package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AttendanceDetailRequest {
    private Long studentId;
    private Double finalScore;
    private Double middleScore;
    private Boolean week1;
    private Boolean week2;
    private Boolean week3;
    private Boolean week4;
    private Boolean week5;
    private Boolean week6;
    private Boolean week7;
    private Boolean week8;
    private Boolean week9;
}
