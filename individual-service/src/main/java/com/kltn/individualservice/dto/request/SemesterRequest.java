package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class SemesterRequest extends PageableRequest {
    private List<EntityStatus> entityStatuses;
    private Long id;
    private Integer year;
    private Integer studentGroup;
    private Integer semester;
    private LocalDate fromDate;
    private LocalDate toDate;
}
