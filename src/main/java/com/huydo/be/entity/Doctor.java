package com.huydo.be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    private String fullName;
    private String phone;
    private String email;

    private String specialization;
    private String degree;
    private Integer yearsOfExperience;

    private String licenseNumber;

    private Boolean isActive;
    private String description;
}

