package com.huydo.be.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StaffProfileResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String department;
    private String position;
}
