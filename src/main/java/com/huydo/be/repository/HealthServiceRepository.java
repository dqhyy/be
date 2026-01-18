package com.huydo.be.repository;

import com.huydo.be.entity.HealthService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthServiceRepository extends JpaRepository<HealthService, Long> {
    List<HealthService> findByIsActiveTrue();
}
