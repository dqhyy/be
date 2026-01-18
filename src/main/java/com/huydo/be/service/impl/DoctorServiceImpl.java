package com.huydo.be.service.impl;

import com.huydo.be.dto.request.DoctorCreationRequest;
import com.huydo.be.dto.request.DoctorUpdateRequest;
import com.huydo.be.dto.response.DoctorProfileResponse;
import com.huydo.be.entity.Doctor;
import com.huydo.be.entity.HealthService;
import com.huydo.be.enums.AccountStatus;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.DoctorRepository;
import com.huydo.be.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final com.huydo.be.repository.AppointmentRepository appointmentRepository;
    private final com.huydo.be.repository.MedicalRecordRepository medicalRecordRepository;
    private final com.huydo.be.repository.BillRepository billRepository;
    private final com.huydo.be.repository.HealthServiceRepository healthServiceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<DoctorProfileResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorProfileResponse createDoctor(DoctorCreationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Doctor doctor = Doctor.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.DOCTOR)
                .citizenIdentificationCard(request.getCitizenIdentificationCard())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhone())
                .specialization(request.getSpecialization())
                .degree(request.getDegree())
                .yearsOfExperience(request.getYearsOfExperience())
                .licenseNumber(request.getLicenseNumber())
                .description(request.getDescription())
                .isActive(true)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToResponse(savedDoctor);
    }

    @Override
    @Transactional
    public DoctorProfileResponse updateDoctor(Long id, DoctorUpdateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (request.getFullName() != null)
            doctor.setFullName(request.getFullName());
        if (request.getPhone() != null)
            doctor.setPhoneNumber(request.getPhone());
        if (request.getSpecialization() != null)
            doctor.setSpecialization(request.getSpecialization());
        if (request.getDegree() != null)
            doctor.setDegree(request.getDegree());
        if (request.getYearsOfExperience() != null)
            doctor.setYearsOfExperience(request.getYearsOfExperience());
        if (request.getLicenseNumber() != null)
            doctor.setLicenseNumber(request.getLicenseNumber());
        if (request.getDescription() != null)
            doctor.setDescription(request.getDescription());
        if (request.getIsActive() != null)
            doctor.setIsActive(request.getIsActive());

        doctor.setUpdatedAt(LocalDateTime.now());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToResponse(savedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }

    private DoctorProfileResponse mapToResponse(Doctor doctor) {
        return DoctorProfileResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .phone(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .degree(doctor.getDegree())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .description(doctor.getDescription())
                .build();
    }

    @Override
    public List<com.huydo.be.dto.response.AppointmentResponse> getDoctorAppointments() {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        com.huydo.be.entity.Account doctorAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<com.huydo.be.entity.Appointments> appointments = appointmentRepository
                .findByDoctorId(doctorAccount.getId()).stream()
                .filter(a -> a.getStatus() != com.huydo.be.enums.AppointmentStatus.PENDING
                        && a.getStatus() != com.huydo.be.enums.AppointmentStatus.REJECTED)
                .collect(Collectors.toList());

        return appointments.stream().map(appt -> {
            com.huydo.be.entity.Account patient = accountRepository.findById(appt.getPatientAccountId()).orElse(null);
            return com.huydo.be.dto.response.AppointmentResponse.builder()
                    .id(appt.getId())
                    .specialty(appt.getSpecialty())
                    .doctorId(appt.getDoctorId())
                    .patientId(appt.getPatientAccountId())
                    .patientName(patient != null ? patient.getFullName() : "Unknown")
                    .patientPhone(patient != null ? patient.getPhoneNumber() : "")
                    .patientGender(patient != null && patient.getGender() != null ? patient.getGender().name() : "")
                    .patientDob(patient != null ? patient.getDateOfBirth() : null)
                    .appointmentDate(appt.getAppointmentDate())
                    .reasonForVisit(appt.getReasonForVisit())
                    .status(appt.getStatus())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateAppointmentStatus(Long appointmentId, String status) {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        com.huydo.be.entity.Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.huydo.be.entity.Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getDoctorId().equals(account.getId()) && account.getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not authorized to update this appointment");
        }

        try {
            appointment.setStatus(com.huydo.be.enums.AppointmentStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status");
        }

        appointmentRepository.save(appointment);

    }

    @Override
    @Transactional
    public void createMedicalRecord(Long appointmentId, com.huydo.be.dto.request.MedicalRecordRequest request) {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        com.huydo.be.entity.Account doctor = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.huydo.be.entity.Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getDoctorId().equals(doctor.getId()) && doctor.getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not authorized to create medical record for this appointment");
        }

        String treatmentText = "";
        Double servicePrice = 500000.0; 
        Double tmpPrice = 0.0;

        if (!request.getServiceIds().isEmpty() && request.getServiceIds() != null) {
            for(Long serviceId : request.getServiceIds()){
            HealthService service = healthServiceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));
            treatmentText = treatmentText + service.getName() + " ";
            tmpPrice += service.getPrice() != null ? service.getPrice().doubleValue() : 0.0;
            }
        }

        if(tmpPrice > 0){
            servicePrice = tmpPrice;
        }

        // if (request.getServiceId() != null) {
        //     com.huydo.be.entity.HealthService service = healthServiceRepository.findById(request.getServiceId())
        //             .orElseThrow(() -> new RuntimeException("Service not found"));
        //     treatmentText = service.getName();
        //     servicePrice = service.getPrice() != null ? service.getPrice().doubleValue() : 0.0;
        // }

        com.huydo.be.entity.MedicalRecord existingRecord = medicalRecordRepository.findByAppointmentId(appointmentId);
        if (existingRecord != null) {
            existingRecord.setDiagnosis(request.getDiagnosis());
            existingRecord.setTreatment(treatmentText);
            existingRecord.setPrescription(request.getPrescription());
            existingRecord.setNotes(request.getNotes());
            medicalRecordRepository.save(existingRecord);
        } else {
            com.huydo.be.entity.MedicalRecord record = com.huydo.be.entity.MedicalRecord.builder()
                    .appointment(appointment)
                    .patientId(appointment.getPatientAccountId())
                    .doctorId(doctor.getId())
                    .diagnosis(request.getDiagnosis())
                    .treatment(treatmentText)
                    .prescription(request.getPrescription())
                    .notes(request.getNotes())
                    .build();
            medicalRecordRepository.save(record);
        }

        appointment.setStatus(com.huydo.be.enums.AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);

        // Create or Update Bill
        com.huydo.be.entity.Bill existingBill = billRepository.findByAppointmentId(appointmentId);
        if (existingBill == null) {
            com.huydo.be.entity.Bill bill = com.huydo.be.entity.Bill.builder()
                    .appointment(appointment)
                    .patientId(appointment.getPatientAccountId())
                    .doctorId(doctor.getId())
                    .amount(servicePrice)
                    .status(com.huydo.be.enums.BillStatus.UNPAID)
                    .createdAt(LocalDateTime.now())
                    .build();
            billRepository.save(bill);
        } else {
            if (existingBill.getStatus() == com.huydo.be.enums.BillStatus.UNPAID) {
                existingBill.setAmount(servicePrice);
                billRepository.save(existingBill);
            }
        }
    }

    @Override
    public List<com.huydo.be.dto.response.PatientProfileResponse> getMyPatients() {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        com.huydo.be.entity.Account doctor = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<com.huydo.be.entity.Appointments> appointments = appointmentRepository
                .findByDoctorId(doctor.getId());

        java.util.Set<Long> patientIds = appointments.stream()
                .map(com.huydo.be.entity.Appointments::getPatientAccountId)
                .collect(Collectors.toSet());

        List<com.huydo.be.entity.Account> patients = accountRepository.findAllById(patientIds);

        return patients.stream().map(patient -> com.huydo.be.dto.response.PatientProfileResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                .dateOfBirth(patient.getDateOfBirth())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .image(patient.getImage())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<com.huydo.be.dto.response.MedicalRecordResponse> getMedicalRecordsByPatientId(
            Long patientId) {
        List<com.huydo.be.entity.MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        return records.stream().map(record -> {
            com.huydo.be.entity.Account doctor = accountRepository.findById(record.getDoctorId()).orElse(null);
            return com.huydo.be.dto.response.MedicalRecordResponse.builder()
                    .id(record.getId())
                    .doctorName(doctor != null ? doctor.getFullName() : "Unknown")
                    .specialty(record.getAppointment().getSpecialty())
                    .clinicName("Phòng khám Đa khoa")
                    .visitDate(record.getAppointment().getAppointmentDate())
                    .diagnosis(record.getDiagnosis())
                    .treatment(record.getTreatment())
                    .prescription(record.getPrescription())
                    .notes(record.getNotes())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public com.huydo.be.dto.response.MedicalRecordResponse updateMedicalRecord(Long recordId,
            com.huydo.be.dto.request.MedicalRecordRequest request) {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        com.huydo.be.entity.Account doctor = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.huydo.be.entity.MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        if (!record.getDoctorId().equals(doctor.getId()) && doctor.getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not authorized to update this medical record");
        }

        if (request.getDiagnosis() != null)
            record.setDiagnosis(request.getDiagnosis());
        if (request.getTreatment() != null)
            record.setTreatment(request.getTreatment());
        if (request.getPrescription() != null)
            record.setPrescription(request.getPrescription());
        if (request.getNotes() != null)
            record.setNotes(request.getNotes());

        record = medicalRecordRepository.save(record);

        com.huydo.be.entity.Account doc = accountRepository.findById(record.getDoctorId()).orElse(null);

        return com.huydo.be.dto.response.MedicalRecordResponse.builder()
                .id(record.getId())
                .doctorName(doc != null ? doc.getFullName() : "Unknown")
                .specialty(record.getAppointment().getSpecialty())
                .clinicName("Phòng khám Đa khoa")
                .visitDate(record.getAppointment().getAppointmentDate())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .prescription(record.getPrescription())
                .notes(record.getNotes())
                .build();

    }
}
