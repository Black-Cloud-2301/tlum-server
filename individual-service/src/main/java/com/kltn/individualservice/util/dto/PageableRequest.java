package com.kltn.individualservice.util.dto;

import com.kltn.individualservice.dto.request.SortCriteria;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class PageableRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer pageNumber;
    private Integer pageSize;
    private List<SortCriteria> sort;
}
