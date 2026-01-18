package com.huydo.be.controller;

import com.huydo.be.dto.request.ApiResponse;
import com.huydo.be.dto.request.BookingRequest;
import com.huydo.be.dto.response.PatientProfileResponse;
import com.huydo.be.entity.Appointments;
import com.huydo.be.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

        private final PatientService patientService;

        @PostMapping("/booking")
        public ApiResponse<Appointments> bookAppointment(
                        @RequestBody @Valid BookingRequest request) {
                return ApiResponse.<Appointments>builder()
                                .result(patientService.bookAppointment(request))
                                .build();
        }

        @GetMapping("/appointments")
        public ApiResponse<java.util.List<com.huydo.be.dto.response.AppointmentResponse>> getMyAppointments() {
                return ApiResponse.<java.util.List<com.huydo.be.dto.response.AppointmentResponse>>builder()
                                .result(patientService.getMyAppointments())
                                .build();
        }

        @GetMapping("/medical-records")
        public ApiResponse<java.util.List<com.huydo.be.dto.response.MedicalRecordResponse>> getMyMedicalRecords() {
                return ApiResponse.<java.util.List<com.huydo.be.dto.response.MedicalRecordResponse>>builder()
                                .result(patientService.getMyMedicalRecords())
                                .build();
        }

        @GetMapping("/bills")
        public ApiResponse<java.util.List<com.huydo.be.dto.response.BillResponse>> getMyBills() {
                return ApiResponse.<java.util.List<com.huydo.be.dto.response.BillResponse>>builder()
                                .result(patientService.getMyBills())
                                .build();
        }

        @GetMapping("/profile")
        public ApiResponse<PatientProfileResponse> getProfile() {
                return ApiResponse.<PatientProfileResponse>builder()
                                .result(patientService.getProfile())
                                .build();
        }

        @PostMapping("/avatar")
        public ApiResponse<String> uploadAvatar(
                        @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
                patientService.uploadAvatar(file);
                return ApiResponse.<String>builder()
                                .result("Avatar uploaded successfully")
                                .build();
        }

        @GetMapping
        public ApiResponse<java.util.List<PatientProfileResponse>> getAllPatients() {
                return ApiResponse.<java.util.List<PatientProfileResponse>>builder()
                                .result(patientService.getAllPatients())
                                .build();
        }

        @PostMapping
        public ApiResponse<PatientProfileResponse> createPatient(
                        @RequestBody @Valid com.huydo.be.dto.request.RegisterRequest request) {
                return ApiResponse.<PatientProfileResponse>builder()
                                .result(patientService.createPatient(request))
                                .build();
        }

        @PutMapping("/{id}")
        public ApiResponse<PatientProfileResponse> updatePatient(@PathVariable Long id,
                        @RequestBody @Valid com.huydo.be.dto.request.RegisterRequest request) {
                return ApiResponse.<PatientProfileResponse>builder()
                                .result(patientService.updatePatient(id, request))
                                .build();
        }

        @DeleteMapping("/{id}")
        public ApiResponse<String> deletePatient(@PathVariable Long id) {
                patientService.deletePatient(id);
                return ApiResponse.<String>builder()
                                .result("Patient deleted successfully")
                                .build();
        }
}
