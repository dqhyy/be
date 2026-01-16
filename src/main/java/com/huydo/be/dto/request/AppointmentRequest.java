package com.huydo.be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentRequest {
    @NotBlank(message = "Chuyên khoa không được để trống")
    private String specialty;

    private Long doctorId;

    @NotNull(message = "Ngày khám không được để trống")
    private LocalDate appointmentDate;

    @NotBlank(message = "Họ và tên không được để trống")
    private String patientName;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dateOfBirth;

    private String email;

    @NotBlank(message = "Lý do khám không được để trống")
    private String reasonForVisit;
}
