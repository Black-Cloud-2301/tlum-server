package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Major;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.MajorRepository;
import com.kltn.individualservice.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    public Major getMajor(Long id) {
        return majorRepository.findById(id).orElse(null);
    }

    @Override
    public Major getMajorByCodeAndIsActive(String code, EntityStatus entityStatus) {
        return majorRepository.findByCodeAndIsActive(code, entityStatus).orElseThrow(() -> new NotFoundException("Major"));
    }
}
