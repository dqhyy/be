package com.huydo.be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Doctor extends Account {
    private String specialization;
    private String degree;
    private Integer yearsOfExperience;
    private String licenseNumber;
    private Boolean isActive;
    private String description;
}
