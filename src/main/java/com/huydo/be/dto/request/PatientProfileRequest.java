package com.huydo.be.dto.request;

import com.huydo.be.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientProfileRequest {

    @NotBlank
    private String fullName;

    private Gender gender;
    private LocalDate dateOfBirth;

    @NotBlank
    private String phoneNumber;

    private String address;
    private String identityNumber;
    private String insuranceNumber;
}

