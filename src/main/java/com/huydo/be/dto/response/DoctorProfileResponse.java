package com.huydo.be.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorProfileResponse {

    private String fullName;
    private String phone;
    private String email;
    private String specialization;
    private String degree;
    private Integer yearsOfExperience;
    private Boolean isActive;
}

