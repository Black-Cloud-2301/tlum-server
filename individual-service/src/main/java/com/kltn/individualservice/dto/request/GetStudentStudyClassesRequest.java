package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetStudentStudyClassesRequest {
    Integer year;
    Integer semester;
    Integer studentGroup;

    @Override
    public String toString() {
        return "GetStudentStudyClassesRequest{" +
                "year=" + year +
                ", semester=" + semester +
                ", studentGroup=" + studentGroup +
                '}';
    }
}
