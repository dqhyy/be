package com.huydo.be.controller;

import com.huydo.be.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/me")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok(doctorService.getMyProfile());
    }
}

