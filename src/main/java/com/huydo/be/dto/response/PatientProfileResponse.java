package com.huydo.be.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PatientProfileResponse {

    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private String identityNumber;
    private String insuranceNumber;
}

