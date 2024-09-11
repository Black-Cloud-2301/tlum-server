package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetStudentStudyClassesRequest {
    Long semesterId;
    Long studentId;

    @Override
    public String toString() {
        return "GetStudentStudyClassesRequest{" +
                "semesterId=" + semesterId +
                ", studentId=" + studentId +
                '}';
    }
}
