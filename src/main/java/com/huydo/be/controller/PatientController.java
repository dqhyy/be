package com.huydo.be.controller;

import com.huydo.be.dto.request.AppointmentRequest;
import com.huydo.be.dto.request.PatientProfileRequest;
import com.huydo.be.service.PatientService;
import com.huydo.be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

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

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/booking")
    public ResponseEntity<?> createOrUpdateBooking(
            @RequestBody @Valid AppointmentRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        Long accountId = userService.getCurrentUser().getId();

        patientService.createAppointment(request, accountId);
        return ResponseEntity.ok("Appointment created");
    }

}
