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
    private String code;
    private String name;
    private List<Long> studyDepartmentIds;
    private List<EmployeeStatus> statuses;
    private List<EntityStatus> entityStatuses;

    @Override
    public String toString() {
        return "GetStudentsRequest{" +
                "pageNumber=" + getPageNumber() +
                ", pageSize=" + getPageSize() +
                ", entityStatuses=" + entityStatuses +
                ", code=" + code +
                ", name=" + name +
                ", studyDepartmentIds=" + studyDepartmentIds +
                ", statuses=" + statuses +
                '}';
    }
}
