package com.huydo.be.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    @NotNull
    private String token;

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String role;
}
