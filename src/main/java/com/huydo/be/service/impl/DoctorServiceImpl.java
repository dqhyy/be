package com.huydo.be.service.impl;

import com.huydo.be.dto.response.DoctorProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Doctor;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.DoctorRepository;
import com.huydo.be.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;

    @Override
    public DoctorProfileResponse getMyProfile() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getRole() != Role.DOCTOR) {
            throw new RuntimeException("Access denied");
        }

        Doctor doctor = doctorRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return DoctorProfileResponse.builder()
                .fullName(doctor.getFullName())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .degree(doctor.getDegree())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .isActive(doctor.getIsActive())
                .build();
    }
}
