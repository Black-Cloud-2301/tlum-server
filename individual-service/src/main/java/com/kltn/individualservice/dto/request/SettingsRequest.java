package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.util.dto.PageableRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SettingsRequest extends PageableRequest {
    private String tableCode;
    private String tableName;
    private String code;
    private String value;
    private List<EntityStatus> entityStatuses;
}
