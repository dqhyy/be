package com.huydo.be.dto.response;

import com.huydo.be.enums.BillStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BillResponse {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String specialty;
    private LocalDate appointmentDate;
    private Double amount;
    private BillStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime paymentDate;
}
