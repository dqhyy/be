package com.huydo.be.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {
    String fullName;
    String phoneNumber;
    String address;
    LocalDate dateOfBirth;
    String gender;
    String citizenIdentificationCard;
}
