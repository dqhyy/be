package com.huydo.be.service.impl;

import com.huydo.be.dto.request.BookingRequest;
import com.huydo.be.dto.response.PatientProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Appointments;
import com.huydo.be.entity.Patient;
import com.huydo.be.enums.AppointmentStatus;
import com.huydo.be.enums.Gender;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.AppointmentRepository;
import com.huydo.be.repository.PatientRepository;
import com.huydo.be.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final com.huydo.be.repository.MedicalRecordRepository medicalRecordRepository;
    private final com.huydo.be.repository.BillRepository billRepository;

    @Override
    @Transactional
    public Appointments bookAppointment(BookingRequest request) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Patient patient = patientRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (request.getPatientName() != null && !request.getPatientName().isEmpty()) {
            patient.setFullName(request.getPatientName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            patient.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            try {
                patient.setGender(Gender.valueOf(request.getGender()));
            } catch (IllegalArgumentException e) {
            }
        }

        patientRepository.save(patient);

        Appointments appointment = Appointments.builder()
                .specialty(request.getSpecialty())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .reasonForVisit(request.getReasonForVisit())
                .patientAccountId(patient.getId())
                .status(AppointmentStatus.PENDING)
                .build();

        return appointmentRepository.save(appointment);
    }

    @Override
    public PatientProfileResponse getProfile() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return PatientProfileResponse.builder()
                .fullName(patient.getFullName())
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                .dateOfBirth(patient.getDateOfBirth())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .identityNumber(patient.getCitizenIdentificationCard())
                .insuranceNumber(patient.getInsuranceNumber())
                .email(patient.getEmail())
                .image(patient.getImage())
                .build();
    }

    @Override
    public void uploadAvatar(org.springframework.web.multipart.MultipartFile file) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file != null && !file.isEmpty()) {
            try {
                account.setImage(file.getBytes());
                accountRepository.save(account);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }
    }

    @Override
    public java.util.List<PatientProfileResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> PatientProfileResponse.builder()
                        .id(patient.getId())
                        .username(patient.getUsername())
                        .fullName(patient.getFullName())
                        .gender(patient.getGender() != null ? patient.getGender().name() : null)
                        .dateOfBirth(patient.getDateOfBirth())
                        .phoneNumber(patient.getPhoneNumber())
                        .address(patient.getAddress())
                        .identityNumber(patient.getCitizenIdentificationCard())
                        .insuranceNumber(patient.getInsuranceNumber())
                        .email(patient.getEmail())
                        .image(patient.getImage())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public PatientProfileResponse createPatient(com.huydo.be.dto.request.RegisterRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Patient patient = Patient.builder()
                .username(request.getUsername())
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10)
                        .encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .role(com.huydo.be.enums.Role.PATIENT)
                .citizenIdentificationCard(request.getCitizenIdentificationCard())
                .insuranceNumber(request.getInsuranceNumber())
                .status(com.huydo.be.enums.AccountStatus.ACTIVE)
                .build();

        if (request.getGender() != null) {
            try {
                patient.setGender(Gender.valueOf(request.getGender()));
            } catch (IllegalArgumentException e) {
            }
        }

        patient = patientRepository.save(patient);

        return PatientProfileResponse.builder()
                .id(patient.getId())
                .username(patient.getUsername())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .build();
    }

    @Override
    @Transactional
    public PatientProfileResponse updatePatient(Long id, com.huydo.be.dto.request.RegisterRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (request.getFullName() != null)
            patient.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null)
            patient.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null)
            patient.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null)
            patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getEmail() != null)
            patient.setEmail(request.getEmail());
        if (request.getCitizenIdentificationCard() != null)
            patient.setCitizenIdentificationCard(request.getCitizenIdentificationCard());
        if (request.getInsuranceNumber() != null)
            patient.setInsuranceNumber(request.getInsuranceNumber());

        if (request.getGender() != null) {
            try {
                patient.setGender(Gender.valueOf(request.getGender()));
            } catch (IllegalArgumentException e) {
            }
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            patient.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10)
                    .encode(request.getPassword()));
        }

        patient = patientRepository.save(patient);

        return PatientProfileResponse.builder()
                .id(patient.getId())
                .username(patient.getUsername())
                .fullName(patient.getFullName())
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                .dateOfBirth(patient.getDateOfBirth())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .email(patient.getEmail())
                .build();
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    @Override
    public java.util.List<com.huydo.be.dto.response.AppointmentResponse> getMyAppointments() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        java.util.List<Appointments> appointments = appointmentRepository.findByPatientAccountId(account.getId());

        return appointments.stream().map(appt -> {
            Account doctor = accountRepository.findById(appt.getDoctorId()).orElse(null);
            return com.huydo.be.dto.response.AppointmentResponse.builder()
                    .id(appt.getId())
                    .specialty(appt.getSpecialty())
                    .doctorId(appt.getDoctorId())
                    .doctorName(doctor != null ? doctor.getFullName() : "Unknown Doctor")
                    .patientId(appt.getPatientAccountId())
                    .patientName(account.getFullName()) // Current user is patient
                    .patientPhone(account.getPhoneNumber())
                    .patientGender(account.getGender() != null ? account.getGender().name() : "")
                    .patientDob(account.getDateOfBirth())
                    .appointmentDate(appt.getAppointmentDate())
                    .reasonForVisit(appt.getReasonForVisit())
                    .status(appt.getStatus())
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<com.huydo.be.dto.response.MedicalRecordResponse> getMyMedicalRecords() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return medicalRecordRepository.findByPatientId(account.getId()).stream().map(record -> {
            Account doctor = accountRepository.findById(record.getDoctorId()).orElse(null);
            return com.huydo.be.dto.response.MedicalRecordResponse.builder()
                    .id(record.getId())
                    .doctorName(doctor != null ? doctor.getFullName() : "Unknown Doctor")
                    .specialty(record.getAppointment().getSpecialty())
                    .clinicName("Phòng khám Đa khoa")
                    .visitDate(record.getAppointment().getAppointmentDate())
                    .diagnosis(record.getDiagnosis())
                    .treatment(record.getTreatment())
                    .prescription(record.getPrescription())
                    .notes(record.getNotes())
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<com.huydo.be.dto.response.BillResponse> getMyBills() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return billRepository.findByPatientId(account.getId()).stream().map(bill -> {
            Account doctor = accountRepository.findById(bill.getDoctorId()).orElse(null);
            return com.huydo.be.dto.response.BillResponse.builder()
                    .id(bill.getId())
                    .appointmentId(bill.getAppointment().getId())
                    .patientName(account.getFullName())
                    .doctorName(doctor != null ? doctor.getFullName() : "Unknown")
                    .specialty(bill.getAppointment().getSpecialty())
                    .appointmentDate(bill.getAppointment().getAppointmentDate())
                    .amount(bill.getAmount())
                    .status(bill.getStatus())
                    .createdAt(bill.getCreatedAt())
                    .paymentDate(bill.getPaymentDate())
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }
}
