package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudentStudyClassRequest {
    private Long id;

    private String attendances;

    private Double middleScore;

    private Double finalScore;
}
