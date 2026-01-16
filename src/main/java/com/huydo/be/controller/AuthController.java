package com.huydo.be.controller;

import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.dto.response.LoginResponse;
import com.huydo.be.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        accountService.register(request);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String token = accountService.login(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

}

