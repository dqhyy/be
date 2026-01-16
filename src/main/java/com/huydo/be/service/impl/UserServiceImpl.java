package com.huydo.be.service.impl;

import com.huydo.be.dto.response.UserProfileResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.repository.AccountRepository;
import com.huydo.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;

    @Override
    public UserProfileResponse getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .role(account.getRole().name())
                .status(account.getStatus().name())
                .build();
    }
}

