package com.huydo.be.entity;

import com.huydo.be.enums.AccountStatus;
import com.huydo.be.enums.Gender;
import com.huydo.be.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String fullName;

    private String phoneNumber;

    private String address;

    private java.time.LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(unique = true)
    private String citizenIdentificationCard;
}
