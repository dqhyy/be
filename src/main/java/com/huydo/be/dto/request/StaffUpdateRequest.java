package com.huydo.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffUpdateRequest {
    private String fullName;
    private String phone;
    private String email;
    private String department;
    private String position;
    private String citizenIdentificationCard;
}
