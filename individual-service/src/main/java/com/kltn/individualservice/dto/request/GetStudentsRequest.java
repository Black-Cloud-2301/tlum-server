package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetStudentsRequest extends PageableRequest {
    private List<StudentStatus> studentStatuses;
    private List<EntityStatus> entityStatuses;
}
