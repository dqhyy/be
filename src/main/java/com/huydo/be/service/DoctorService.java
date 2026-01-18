package com.huydo.be.service;

import com.huydo.be.dto.response.DoctorProfileResponse;

import java.util.List;

public interface DoctorService {
    List<DoctorProfileResponse> getAllDoctors();

    DoctorProfileResponse createDoctor(com.huydo.be.dto.request.DoctorCreationRequest request);

    DoctorProfileResponse updateDoctor(Long id, com.huydo.be.dto.request.DoctorUpdateRequest request);

    void deleteDoctor(Long id);

    List<com.huydo.be.dto.response.AppointmentResponse> getDoctorAppointments();

    void updateAppointmentStatus(Long appointmentId, String status);

    void createMedicalRecord(Long appointmentId, com.huydo.be.dto.request.MedicalRecordRequest request);

    List<com.huydo.be.dto.response.PatientProfileResponse> getMyPatients();

    List<com.huydo.be.dto.response.MedicalRecordResponse> getMedicalRecordsByPatientId(Long patientId);

    com.huydo.be.dto.response.MedicalRecordResponse updateMedicalRecord(Long recordId,
            com.huydo.be.dto.request.MedicalRecordRequest request);
}
