package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.entity.Major;
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
}
