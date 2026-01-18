package com.huydo.be.repository;

import com.huydo.be.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatientAccountId(Long patientAccountId);

    List<Appointments> findByDoctorId(Long doctorId);
}
