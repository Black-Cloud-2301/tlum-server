package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.GetMajorsRequest;
import com.kltn.individualservice.entity.Major;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.repository.MajorRepository;
import com.kltn.individualservice.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    @Override
    public List<Major> getMajors(GetMajorsRequest request) {
        return this.majorRepository.findByIsActiveIn(request.getEntityStatuses());
    }

    @Override
    public Page<Major> getMajors(GetMajorsRequest request, Pageable pageable) {
        return this.majorRepository.findByIsActiveIn(request.getEntityStatuses(), pageable);
    }

    @Override
    public Major getMajor(Long id) {
        return majorRepository.findById(id).orElse(null);
    }

    @Override
    public Major getMajorByCodeAndIsActive(String code, EntityStatus entityStatus) {
        return majorRepository.findByCodeAndIsActive(code, entityStatus).orElseThrow(() -> new NotFoundException("Major"));
    }

    @Override
    public List<Major> getMajorByIds(List<Long> ids) {
        return majorRepository.findAllById(ids);
    }
}
