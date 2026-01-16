package com.huydo.be.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String status;
}
