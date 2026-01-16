package com.huydo.be.controller;

import com.huydo.be.dto.request.PatientProfileRequest;
import com.huydo.be.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok(patientService.getMyProfile());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdate(
            @RequestBody @Valid PatientProfileRequest request
    ) {
        patientService.createOrUpdateProfile(request);
        return ResponseEntity.ok("Patient profile saved");
    }

}
