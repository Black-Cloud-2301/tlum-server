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
    private String code;
    private String name;
    private List<Long> majorIds;
    private List<StudentStatus> statuses;
    private List<EntityStatus> entityStatuses;
    private Long semesterId;

    @Override
    public String toString() {
        return "GetStudentsRequest{" +
                "pageNumber=" + getPageNumber() +
                ", pageSize=" + getPageSize() +
                ", entityStatuses=" + entityStatuses +
                ", code=" + code +
                ", name=" + name +
                ", majorIds=" + majorIds +
                ", statuses=" + statuses +
                '}';
    }
}
