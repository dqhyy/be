package com.huydo.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private String specialty;
    private Long doctorId;
    private LocalDate appointmentDate;
    private String patientName;
    private String gender; 
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String email;
    private String reasonForVisit;
}
