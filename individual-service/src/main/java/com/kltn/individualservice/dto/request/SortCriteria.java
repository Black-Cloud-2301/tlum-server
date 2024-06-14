package com.kltn.individualservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class SortCriteria {
    private String field;
    private Sort.Direction direction;
}