package com.huydo.be.controller;

import com.huydo.be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/me")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok(staffService.getMyProfile());
    }

}

