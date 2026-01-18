package com.huydo.be.entity;

import com.huydo.be.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "doctor_id")
    private Long doctorId;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @NotBlank
    @Column(name = "reason_for_visit", nullable = false, columnDefinition = "TEXT")
    private String reasonForVisit;

    @Column(name = "patient_account_id")
    private Long patientAccountId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "appointment_order")
    private Integer appointmentOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", length = 32)
    private AppointmentStatus status;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}