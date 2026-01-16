package com.huydo.be.repository;

import com.huydo.be.entity.Account;
import com.huydo.be.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
}
