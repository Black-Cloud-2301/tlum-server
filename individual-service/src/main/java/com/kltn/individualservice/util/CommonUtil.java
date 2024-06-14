package com.kltn.individualservice.util;

import com.kltn.individualservice.dto.request.SortCriteria;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.util.dto.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommonUtil {
    public Set<String> getPermissionsFromRoles(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getModule() + "/" + permission.getFunction() + "/" + permission.getAction())
                .collect(Collectors.toSet());
    }

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
