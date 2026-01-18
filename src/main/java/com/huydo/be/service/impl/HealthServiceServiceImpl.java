package com.huydo.be.service.impl;

import com.huydo.be.dto.request.HealthServiceRequest;
import com.huydo.be.entity.HealthService;
import com.huydo.be.repository.HealthServiceRepository;
import com.huydo.be.service.HealthServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthServiceServiceImpl implements HealthServiceService {

    private final HealthServiceRepository healthServiceRepository;

    @Override
    public List<HealthService> getAllHealthServices() {
        return healthServiceRepository.findAll();
    }

    @Override
    @Transactional
    public HealthService createHealthService(HealthServiceRequest request) {
        HealthService healthService = HealthService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        return healthServiceRepository.save(healthService);
    }

    @Override
    @Transactional
    public HealthService updateHealthService(Long id, HealthServiceRequest request) {
        HealthService healthService = healthServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        healthService.setName(request.getName());
        healthService.setDescription(request.getDescription());
        healthService.setPrice(request.getPrice());
        healthService.setDurationMinutes(request.getDurationMinutes());
        if (request.getIsActive() != null) {
            healthService.setIsActive(request.getIsActive());
        }

        return healthServiceRepository.save(healthService);
    }

    @Override
    @Transactional
    public void deleteHealthService(Long id) {
        if (!healthServiceRepository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        healthServiceRepository.deleteById(id);
    }
}
