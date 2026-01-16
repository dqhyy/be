package com.huydo.be.service;

import com.huydo.be.dto.request.PatientProfileRequest;
import com.huydo.be.dto.response.PatientProfileResponse;

public interface PatientService {
    PatientProfileResponse getMyProfile();
    void createOrUpdateProfile(PatientProfileRequest request);
}

