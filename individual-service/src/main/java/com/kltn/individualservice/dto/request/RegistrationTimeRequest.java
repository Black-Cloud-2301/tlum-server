package com.kltn.individualservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RegistrationTimeRequest extends PageableRequest {
    private Long id;
    private Long semesterId;
    private Long studentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String code;
    private String name;

    @Override
    public String toString() {
        return "SemesterRequest{" +
                "pageNumber=" + getPageNumber() +
                ", pageSize=" + getPageSize() +
                ", id=" + id +
                ", semesterId=" + semesterId +
                ", studentId=" + studentId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", code=" + code +
                ", name=" + name +
                '}';
    }
}
