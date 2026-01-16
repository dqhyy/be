package com.huydo.be.service;

import com.huydo.be.dto.request.AppointmentRequest;
import com.huydo.be.dto.request.PatientProfileRequest;
import com.huydo.be.dto.response.PatientProfileResponse;
import jakarta.validation.Valid;

public interface PatientService {
    PatientProfileResponse getMyProfile();
    void createOrUpdateProfile(PatientProfileRequest request);

    void createAppointment(@Valid AppointmentRequest request, Long accountId);
}

