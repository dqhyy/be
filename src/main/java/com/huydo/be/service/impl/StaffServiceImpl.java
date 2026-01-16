package com.huydo.be.service.impl;

import com.huydo.be.dto.response.PatientProfileResponse;
import com.huydo.be.dto.response.StaffProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Patient;
import com.huydo.be.entity.Staff;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.PatientRepository;
import com.huydo.be.repository.StaffRepository;
import com.huydo.be.service.PatientService;
import com.huydo.be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final PatientRepository patientRepository;
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;

    @Override
    public StaffProfileResponse getMyProfile() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getRole() != Role.PATIENT) {
            throw new RuntimeException("Access denied");
        }

        Staff staff = staffRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return StaffProfileResponse.builder()
                .fullName(staff.getFullName())
                .phone(staff.getPhone())
                .email(staff.getEmail())
                .department(staff.getDepartment())
                .position(staff.getPosition())
                .build();
    }
}

