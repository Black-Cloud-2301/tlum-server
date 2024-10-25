package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class SubjectsRequest extends PageableRequest {
    private List<EntityStatus> entityStatuses;
    private String code;
    private String name;
    private List<Long> majorIds;
}
