package com.huydo.be.service;

public interface StaffService {
    java.util.List<com.huydo.be.dto.response.AppointmentResponse> getAllAppointments();

    com.huydo.be.dto.response.AppointmentResponse confirmAppointment(Long appointmentId, Long doctorId);

    void cancelAppointment(Long appointmentId);

    java.util.List<com.huydo.be.dto.response.StaffProfileResponse> getAllStaffs();

    com.huydo.be.dto.response.StaffProfileResponse createStaff(com.huydo.be.dto.request.StaffCreationRequest request);

    com.huydo.be.dto.response.StaffProfileResponse updateStaff(Long id,
            com.huydo.be.dto.request.StaffUpdateRequest request);

    void deleteStaff(Long id);

    void updateAppointmentOrders(java.util.List<com.huydo.be.dto.request.AppointmentOrderDTO> orders);

    java.util.List<com.huydo.be.dto.response.BillResponse> getAllBills();

    com.huydo.be.dto.response.BillResponse confirmPayment(Long billId);
}
