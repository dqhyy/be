package com.huydo.be.service;

import com.huydo.be.dto.request.BookingRequest;
import com.huydo.be.dto.response.PatientProfileResponse;
import com.huydo.be.entity.Appointments;

public interface PatientService {
    Appointments bookAppointment(BookingRequest request);

    PatientProfileResponse getProfile();

    void uploadAvatar(org.springframework.web.multipart.MultipartFile file);

    java.util.List<PatientProfileResponse> getAllPatients();

    PatientProfileResponse createPatient(com.huydo.be.dto.request.RegisterRequest request);

    PatientProfileResponse updatePatient(Long id,
            com.huydo.be.dto.request.RegisterRequest request);

    void deletePatient(Long id);

    java.util.List<com.huydo.be.dto.response.AppointmentResponse> getMyAppointments();

    java.util.List<com.huydo.be.dto.response.MedicalRecordResponse> getMyMedicalRecords();

    java.util.List<com.huydo.be.dto.response.BillResponse> getMyBills();
}
