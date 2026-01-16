package com.huydo.be.repository;

import com.huydo.be.entity.Account;
import com.huydo.be.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByAccount(Account account);
}

