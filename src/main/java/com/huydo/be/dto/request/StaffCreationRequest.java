package com.huydo.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffCreationRequest {
    private String username;
    private String password;
    private String email;
    private String citizenIdentificationCard;

    private String fullName;
    private String phone;
    private String department;
    private String position;
}
