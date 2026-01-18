package com.huydo.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateRequest {
    private String fullName;
    private String phone;
    private String specialization;
    private String degree;
    private Integer yearsOfExperience;
    private String licenseNumber;
    private String description;
    private Boolean isActive;
}
