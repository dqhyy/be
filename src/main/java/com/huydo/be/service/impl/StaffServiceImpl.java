package com.huydo.be.service.impl;

import com.huydo.be.dto.response.StaffProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Staff;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.repository.StaffRepository;
import com.huydo.be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final com.huydo.be.repository.AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final com.huydo.be.repository.BillRepository billRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public List<StaffProfileResponse> getAllStaffs() {
        return staffRepository.findAll().stream().map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public StaffProfileResponse createStaff(com.huydo.be.dto.request.StaffCreationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Staff staff = Staff.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .citizenIdentificationCard(request.getCitizenIdentificationCard())
                .role(Role.STAFF)
                .status(com.huydo.be.enums.AccountStatus.ACTIVE)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhone())
                .department(request.getDepartment())
                .position(request.getPosition())
                .build();

        staff = staffRepository.save(staff);
        return mapToResponse(staff);
    }

    @Override
    @Transactional
    public StaffProfileResponse updateStaff(Long id, com.huydo.be.dto.request.StaffUpdateRequest request) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));

        if (request.getFullName() != null)
            staff.setFullName(request.getFullName());
        if (request.getPhone() != null)
            staff.setPhoneNumber(request.getPhone());
        if (request.getEmail() != null)
            staff.setEmail(request.getEmail());
        if (request.getDepartment() != null)
            staff.setDepartment(request.getDepartment());
        if (request.getPosition() != null)
            staff.setPosition(request.getPosition());
        if (request.getCitizenIdentificationCard() != null)
            staff.setCitizenIdentificationCard(request.getCitizenIdentificationCard());

        staff.setUpdatedAt(java.time.LocalDateTime.now());
        staff = staffRepository.save(staff);
        return mapToResponse(staff);
    }

    @Override
    @Transactional
    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new RuntimeException("Staff not found");
        }
        staffRepository.deleteById(id);
    }

    private StaffProfileResponse mapToResponse(Staff staff) {
        return StaffProfileResponse.builder()
                .id(staff.getId())
                .fullName(staff.getFullName())
                .phone(staff.getPhoneNumber())
                .email(staff.getEmail())
                .department(staff.getDepartment())
                .position(staff.getPosition())
                .build();
    }

    @Override
    public List<com.huydo.be.dto.response.AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appt -> {
            Account doctor = accountRepository.findById(appt.getDoctorId()).orElse(null);
            Account patient = accountRepository.findById(appt.getPatientAccountId()).orElse(null);
            return com.huydo.be.dto.response.AppointmentResponse.builder()
                    .id(appt.getId())
                    .specialty(appt.getSpecialty())
                    .doctorId(appt.getDoctorId())
                    .doctorName(doctor != null ? doctor.getFullName() : "Unknown Doctor")
                    .patientId(appt.getPatientAccountId())
                    .patientName(patient != null ? patient.getFullName() : "Unknown Patient")
                    .patientPhone(patient != null ? patient.getPhoneNumber() : "")
                    .patientGender(patient != null && patient.getGender() != null ? patient.getGender().name() : "")
                    .patientDob(patient != null ? patient.getDateOfBirth() : null)
                    .appointmentDate(appt.getAppointmentDate())
                    .reasonForVisit(appt.getReasonForVisit())
                    .status(appt.getStatus())
                    .appointmentOrder(appt.getAppointmentOrder())
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public com.huydo.be.dto.response.AppointmentResponse confirmAppointment(Long appointmentId, Long doctorId) {
        com.huydo.be.entity.Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (doctorId != null) {
            appointment.setDoctorId(doctorId);
        }

        appointment.setStatus(com.huydo.be.enums.AppointmentStatus.CONFIRMED);
        appointment = appointmentRepository.save(appointment);

        Account doctor = accountRepository.findById(appointment.getDoctorId()).orElse(null);
        Account patient = accountRepository.findById(appointment.getPatientAccountId()).orElse(null);

        return com.huydo.be.dto.response.AppointmentResponse.builder()
                .id(appointment.getId())
                .specialty(appointment.getSpecialty())
                .doctorId(appointment.getDoctorId())
                .doctorName(doctor != null ? doctor.getFullName() : "Unknown")
                .patientId(appointment.getPatientAccountId())
                .patientName(patient != null ? patient.getFullName() : "Unknown")
                .status(appointment.getStatus())
                .appointmentDate(appointment.getAppointmentDate())
                .build();
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        com.huydo.be.entity.Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(com.huydo.be.enums.AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void updateAppointmentOrders(List<com.huydo.be.dto.request.AppointmentOrderDTO> orders) {
        for (com.huydo.be.dto.request.AppointmentOrderDTO order : orders) {
            com.huydo.be.entity.Appointments appt = appointmentRepository.findById(order.getId())
                    .orElse(null);
            if (appt != null) {
                if (order.getDoctorId() != null) {
                    appt.setDoctorId(order.getDoctorId());
                    // If previously PENDING, set to CONFIRMED when assigned to doctor
                    if (appt.getStatus() == com.huydo.be.enums.AppointmentStatus.PENDING) {
                        appt.setStatus(com.huydo.be.enums.AppointmentStatus.CONFIRMED);
                    }
                } else {
                    if (order.getDoctorId() == null) {
                        appt.setDoctorId(null);
                    }
                }
                appt.setAppointmentOrder(order.getOrder());
                appointmentRepository.save(appt);
            }
        }
    }

    @Override
    public List<com.huydo.be.dto.response.BillResponse> getAllBills() {
        return billRepository.findAll().stream().map(bill -> {
            Account doctor = accountRepository.findById(bill.getDoctorId()).orElse(null);
            Account patient = accountRepository.findById(bill.getPatientId()).orElse(null);
            return com.huydo.be.dto.response.BillResponse.builder()
                    .id(bill.getId())
                    .appointmentId(bill.getAppointment().getId())
                    .patientName(patient != null ? patient.getFullName() : "Unknown")
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

    @Override
    @Transactional
    public com.huydo.be.dto.response.BillResponse confirmPayment(Long billId) {
        com.huydo.be.entity.Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setStatus(com.huydo.be.enums.BillStatus.PAID);
        bill.setPaymentDate(java.time.LocalDateTime.now());

        bill = billRepository.save(bill);

        Account doctor = accountRepository.findById(bill.getDoctorId()).orElse(null);
        Account patient = accountRepository.findById(bill.getPatientId()).orElse(null);

        return com.huydo.be.dto.response.BillResponse.builder()
                .id(bill.getId())
                .appointmentId(bill.getAppointment().getId())
                .patientName(patient != null ? patient.getFullName() : "Unknown")
                .doctorName(doctor != null ? doctor.getFullName() : "Unknown")
                .specialty(bill.getAppointment().getSpecialty())
                .appointmentDate(bill.getAppointment().getAppointmentDate())
                .amount(bill.getAmount())
                .status(bill.getStatus())
                .createdAt(bill.getCreatedAt())
                .paymentDate(bill.getPaymentDate())
                .build();
    }
}
