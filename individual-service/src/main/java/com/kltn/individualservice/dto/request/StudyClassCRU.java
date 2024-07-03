package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyClassCRU {
    private Long id;
    private String name;
    private Integer semester;
    private Integer studentGroup;
    private Integer year;
    private String classesOfWeek;
    private Long teacherId;
    private Integer totalStudent;
    private Long subjectId;
}
