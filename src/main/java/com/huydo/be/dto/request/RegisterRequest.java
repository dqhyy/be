package com.huydo.be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;

    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String citizenIdentificationCard;
    private String insuranceNumber;
}
