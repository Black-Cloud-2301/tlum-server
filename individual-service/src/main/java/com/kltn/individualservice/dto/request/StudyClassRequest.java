package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudyClassRequest extends PageableRequest {
    private Integer year;
    private Integer studentGroup;
    private Integer semester;
    private String name;
    private Long subjectId;
    private Long teacherId;
    private List<EntityStatus> entityStatuses;


    @Override
    public String toString() {
        return "StudyClassRequest{" +
                "pageNumber=" + getPageNumber() +
                ", pageSize=" + getPageSize() +
                ", year=" + year +
                ", semester=" + semester +
                ", studentGroup=" + studentGroup +
                ", name=" + name +
                ", subjectId=" + subjectId +
                ", teacherId=" + teacherId +
                ", entityStatuses=" + entityStatuses +
                '}';
    }
}
