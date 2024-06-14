package com.kltn.individualservice.util.dto;

import com.kltn.individualservice.dto.request.SortCriteria;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageableRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private List<SortCriteria> sort;
}
