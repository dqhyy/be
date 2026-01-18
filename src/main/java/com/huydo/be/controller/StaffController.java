package com.huydo.be.controller;

import com.huydo.be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/appointments")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(staffService.getAllAppointments());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllStaffs() {
        return ResponseEntity.ok(staffService.getAllStaffs());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createStaff(
            @RequestBody com.huydo.be.dto.request.StaffCreationRequest request) {
        return ResponseEntity.ok(staffService.createStaff(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStaff(@PathVariable Long id,
            @RequestBody com.huydo.be.dto.request.StaffUpdateRequest request) {
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/appointments/{id}/confirm")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> confirmAppointment(@PathVariable Long id,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(staffService.confirmAppointment(id, doctorId));
    }

    @PostMapping("/appointments/{id}/cancel")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        staffService.cancelAppointment(id);
        return ResponseEntity.ok("Cancelled");
    }

    @PutMapping("/appointments/reorder")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> reorderAppointments(
            @RequestBody java.util.List<com.huydo.be.dto.request.AppointmentOrderDTO> orders) {
        staffService.updateAppointmentOrders(orders);
        return ResponseEntity.ok("Orders updated");
    }

    @GetMapping("/bills")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> getAllBills() {
        return ResponseEntity.ok(staffService.getAllBills());
    }

    @PostMapping("/bills/{id}/pay")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> confirmPayment(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.confirmPayment(id));
    }
}
