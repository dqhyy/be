package com.huydo.be.service;

import com.huydo.be.dto.request.HealthServiceRequest;
import com.huydo.be.entity.HealthService;

import java.util.List;

public interface HealthServiceService {
    List<HealthService> getAllHealthServices();

    HealthService createHealthService(HealthServiceRequest request);

    HealthService updateHealthService(Long id, HealthServiceRequest request);

    void deleteHealthService(Long id);
}
