package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetTeachersRequest extends PageableRequest {
    private List<EmployeeStatus> employeeStatuses;
    private List<EntityStatus> entityStatuses;
}
