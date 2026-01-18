package com.huydo.be.controller;

import com.huydo.be.dto.request.ApiResponse;
import com.huydo.be.dto.request.HealthServiceRequest;
import com.huydo.be.entity.HealthService;
import com.huydo.be.service.HealthServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class HealthServiceController {

    private final HealthServiceService healthServiceService;

    @GetMapping
    public ApiResponse<List<HealthService>> getAllServices() {
        return ApiResponse.<List<HealthService>>builder()
                .result(healthServiceService.getAllHealthServices())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<HealthService> createService(@RequestBody HealthServiceRequest request) {
        return ApiResponse.<HealthService>builder()
                .result(healthServiceService.createHealthService(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<HealthService> updateService(@PathVariable Long id, @RequestBody HealthServiceRequest request) {
        return ApiResponse.<HealthService>builder()
                .result(healthServiceService.updateHealthService(id, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteService(@PathVariable Long id) {
        healthServiceService.deleteHealthService(id);
        return ApiResponse.<String>builder()
                .result("Service has been deleted")
                .build();
    }
}
