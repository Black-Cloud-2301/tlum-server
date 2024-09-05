package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyClassCRU {
    private Long id;
    private String name;
    private Long semesterId;
    private String classesOfWeek;
    private Long teacherId;
    private Integer totalStudent;
    private Long subjectId;
}
