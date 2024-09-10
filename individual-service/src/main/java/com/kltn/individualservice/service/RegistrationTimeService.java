package com.kltn.individualservice.service;

import com.kltn.individualservice.dto.request.RegistrationTimeRequest;
import com.kltn.individualservice.entity.RegistrationTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegistrationTimeService {
    RegistrationTime create(RegistrationTimeRequest request);
    RegistrationTime update(RegistrationTimeRequest request);
    List<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request);
    Page<RegistrationTime> searchBySemesterId(Long semesterId, RegistrationTimeRequest request, Pageable pageable);
    void delete(Long id);
}
