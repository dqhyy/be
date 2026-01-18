package com.huydo.be.dto.response;

import com.huydo.be.enums.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AppointmentResponse {
    private Long id;
    private String specialty;
    private Long doctorId;
    private Long patientId;
    private String patientName;
    private String patientPhone;
    private String patientGender;
    private LocalDate patientDob;
    private LocalDate appointmentDate;
    private String reasonForVisit;
    private String doctorName;
    private AppointmentStatus status;
    private Integer appointmentOrder;
}
