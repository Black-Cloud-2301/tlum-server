package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetMajorsRequest;
import com.kltn.individualservice.entity.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MajorService {
    List<Major> getMajors(GetMajorsRequest request);

    Page<Major> getMajors(GetMajorsRequest request, Pageable pageable);

    Major getMajor(Long id);

    Major getMajorByCodeAndIsActive(String code, EntityStatus entityStatus);
}
