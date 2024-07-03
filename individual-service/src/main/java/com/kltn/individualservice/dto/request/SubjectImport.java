package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubjectImport {
    private String code;
    private String name;
    private Integer credit;
    private Integer requireCredit;
    private Integer hours;
    private Double coefficient;
    private List<String> requireSubjects;
}
