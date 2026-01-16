package com.huydo.be.repository;

import com.huydo.be.entity.Account;
import com.huydo.be.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByAccount(Account account);
}

