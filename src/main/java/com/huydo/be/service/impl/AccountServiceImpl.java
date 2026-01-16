package com.huydo.be.service.impl;

import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.entity.Account;
import com.huydo.be.enums.AccountStatus;
import com.huydo.be.enums.Role;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.security.jwt.JwtUtil;
import com.huydo.be.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.PATIENT)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);
    }

    @Override
    public String login(LoginRequest request) {

        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        account.setLastLogin(LocalDateTime.now());
        accountRepository.save(account);

        return jwtUtil.generateToken(account);
    }

}

