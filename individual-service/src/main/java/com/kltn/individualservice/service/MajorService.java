package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Major;

import java.util.Optional;

public interface MajorService {
    Major getMajor(Long id);
    Major getMajorByCodeAndIsActive(String code, EntityStatus entityStatus);
}
