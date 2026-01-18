package com.huydo.be.controller;

import com.huydo.be.dto.request.ApiResponse;
import com.huydo.be.dto.request.DoctorCreationRequest;
import com.huydo.be.dto.request.DoctorUpdateRequest;
import com.huydo.be.dto.response.DoctorProfileResponse;
import com.huydo.be.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

        private final DoctorService doctorService;

        @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'STAFF')")
        @GetMapping
        public ApiResponse<List<DoctorProfileResponse>> getAllDoctors() {
                return ApiResponse.<List<DoctorProfileResponse>>builder()
                                .result(doctorService.getAllDoctors())
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ApiResponse<DoctorProfileResponse> createDoctor(
                        @RequestBody DoctorCreationRequest request) {
                return ApiResponse.<DoctorProfileResponse>builder()
                                .result(doctorService.createDoctor(request))
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{id}")
        public ApiResponse<DoctorProfileResponse> updateDoctor(
                        @PathVariable Long id,
                        @RequestBody DoctorUpdateRequest request) {
                return ApiResponse.<DoctorProfileResponse>builder()
                                .result(doctorService.updateDoctor(id, request))
                                .build();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{id}")
        public ApiResponse<String> deleteDoctor(@PathVariable Long id) {
                doctorService.deleteDoctor(id);
                return ApiResponse.<String>builder()
                                .result("Doctor has been deleted")
                                .build();
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @GetMapping("/my-appointments")
        public ApiResponse<List<com.huydo.be.dto.response.AppointmentResponse>> getMyAppointments() {
                return ApiResponse.<List<com.huydo.be.dto.response.AppointmentResponse>>builder()
                                .result(doctorService.getDoctorAppointments())
                                .build();
        }

        @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
        @PutMapping("/appointments/{id}/status")
        public ApiResponse<String> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
                doctorService.updateAppointmentStatus(id, status);
                return ApiResponse.<String>builder()
                                .result("Appointment status updated")
                                .build();
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @PostMapping("/appointments/{id}/medical-record")
        public ApiResponse<String> createMedicalRecord(@PathVariable Long id,
                        @RequestBody com.huydo.be.dto.request.MedicalRecordRequest request) {
                doctorService.createMedicalRecord(id, request);
                return ApiResponse.<String>builder()
                                .result("Medical record created successfully")
                                .build();
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @GetMapping("/my-patients")
        public ApiResponse<List<com.huydo.be.dto.response.PatientProfileResponse>> getMyPatients() {
                return ApiResponse.<List<com.huydo.be.dto.response.PatientProfileResponse>>builder()
                                .result(doctorService.getMyPatients())
                                .build();
        }

        @PreAuthorize("hasAnyRole('DOCTOR', 'STAFF')")
        @GetMapping("/patients/{patientId}/medical-records")
        public ApiResponse<List<com.huydo.be.dto.response.MedicalRecordResponse>> getMedicalRecordsByPatient(
                        @PathVariable Long patientId) {
                return ApiResponse.<List<com.huydo.be.dto.response.MedicalRecordResponse>>builder()
                                .result(doctorService.getMedicalRecordsByPatientId(patientId))
                                .build();
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @PutMapping("/medical-records/{recordId}")
        public ApiResponse<com.huydo.be.dto.response.MedicalRecordResponse> updateMedicalRecord(
                        @PathVariable Long recordId,
                        @RequestBody com.huydo.be.dto.request.MedicalRecordRequest request) {
                return ApiResponse.<com.huydo.be.dto.response.MedicalRecordResponse>builder()
                                .result(doctorService.updateMedicalRecord(recordId, request))
                                .build();
        }
}
