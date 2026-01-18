package com.huydo.be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponse {
    private Long id;
    private String doctorName;
    private String specialty;
    private String clinicName; 
    private LocalDate visitDate;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
}
