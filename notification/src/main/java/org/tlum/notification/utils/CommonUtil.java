package org.tlum.notification.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.tlum.notification.dto.request.PageableRequest;
import org.tlum.notification.dto.request.SortCriteria;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonUtil {
       public static Pageable createPageable(PageableRequest request) {
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            List<Sort.Order> orders = new ArrayList<>();
            for (SortCriteria criteria : request.getSort()) {
                orders.add(new Sort.Order(criteria.getDirection(), criteria.getField()));
            }
            Sort sort = Sort.by(orders);
            return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        } else {
            return Pageable.unpaged();
        }
    }
}
