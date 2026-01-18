package com.huydo.be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "health_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private Integer durationMinutes;

    @Builder.Default
    private Boolean isActive = true;
}
