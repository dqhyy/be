package com.huydo.be.service.impl;

import com.huydo.be.dto.request.AppointmentRequest;
import com.huydo.be.dto.request.PatientProfileRequest;
import com.huydo.be.dto.response.PatientProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Appointments;
import com.huydo.be.entity.Patient;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.AppointmentRepository;
import com.huydo.be.repository.PatientRepository;
import com.huydo.be.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AccountRepository accountRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PatientProfileResponse getMyProfile() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getRole() != Role.PATIENT) {
            throw new RuntimeException("Access denied");
        }

        Patient patient = patientRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return PatientProfileResponse.builder()
                .fullName(patient.getFullName())
                .gender(patient.getGender().name())
                .dateOfBirth(patient.getDateOfBirth())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .identityNumber(patient.getIdentityNumber())
                .insuranceNumber(patient.getInsuranceNumber())
                .build();
    }

    @Override
    public void createOrUpdateProfile(PatientProfileRequest request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getRole() != Role.PATIENT) {
            throw new RuntimeException("Access denied");
        }

        Patient patient = patientRepository.findByAccount(account)
                .orElse(Patient.builder().account(account).build());

        patient.setFullName(request.getFullName());
        patient.setGender(request.getGender());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAddress(request.getAddress());
        patient.setIdentityNumber(request.getIdentityNumber());
        patient.setInsuranceNumber(request.getInsuranceNumber());

        patientRepository.save(patient);
    }

    @Override
    public void createAppointment(AppointmentRequest request, Long accountId) {
        Appointments appointments = Appointments.builder()
                .appointmentDate(request.getAppointmentDate())
                .specialty(request.getSpecialty())
                .doctorId(request.getDoctorId())
                .patientAccountId(accountId)
                .build();
        appointmentRepository.save(appointments);
    }

}

