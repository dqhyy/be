package com.huydo.be.repository;

import com.huydo.be.entity.Account;
import com.huydo.be.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByAccount(Account account);
}

