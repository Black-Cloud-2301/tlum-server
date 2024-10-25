package com.kltn.individualservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class RegistrationTimeRequest extends PageableRequest {
    private Long id;
    private Long semesterId;
    private Long studentId;
    private Instant startTime;
    private Instant endTime;
    private String code;
    private String name;
}
