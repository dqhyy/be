package com.huydo.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequest {
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private List<Long> serviceIds;
}
