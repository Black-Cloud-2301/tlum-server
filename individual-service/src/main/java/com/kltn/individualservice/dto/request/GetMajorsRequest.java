package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GetMajorsRequest extends PageableRequest {
    private List<EntityStatus> entityStatuses;

    public GetMajorsRequest(List<EntityStatus> entityStatuses) {
        this.entityStatuses = entityStatuses;
    }
}
