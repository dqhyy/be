package com.huydo.be.service.impl;

import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.dto.response.LoginResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.entity.Patient;
import com.huydo.be.enums.AccountStatus;
import com.huydo.be.enums.Gender;
import com.huydo.be.enums.Role;
import com.huydo.be.exception.AppException;
import com.huydo.be.exception.ErrorCode;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.security.jwt.JwtUtil;
import com.huydo.be.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Account register(RegisterRequest request) {

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Patient patient = Patient.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .gender(Gender.valueOf(request.getGender()))
                .role(Role.PATIENT)
                .citizenIdentificationCard(request.getCitizenIdentificationCard())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (request.getGender() != null) {
            try {
                patient.setGender(Gender.valueOf(request.getGender()));
            } catch (IllegalArgumentException e) {
            }
        }

        return accountRepository.save(patient);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        account.setLastLogin(LocalDateTime.now());
        accountRepository.save(account);

        String token = jwtUtil.generateToken(account);
        return new LoginResponse(token, account.getId(), account.getUsername(), account.getRole().name());
    }

    @Override
    @Transactional
    public void uploadAvatar(MultipartFile file) {
        var context = SecurityContextHolder.getContext();
        String username = Objects.requireNonNull(context.getAuthentication()).getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        try {
            account.setImage(file.getBytes());
            accountRepository.save(account);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    @Override
    public Account getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = Objects.requireNonNull(context.getAuthentication()).getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    @Transactional
    public Account updateMyInfo(com.huydo.be.dto.request.AccountUpdateRequest request) {
        var context = SecurityContextHolder.getContext();
        String username = Objects.requireNonNull(context.getAuthentication()).getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getFullName() != null && !request.getFullName().isEmpty())
            account.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null)
            account.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null)
            account.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null)
            account.setDateOfBirth(request.getDateOfBirth());
        if (request.getCitizenIdentificationCard() != null)
            account.setCitizenIdentificationCard(request.getCitizenIdentificationCard());

        if (request.getGender() != null) {
            try {
                account.setGender(Gender.valueOf(request.getGender()));
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }
}
