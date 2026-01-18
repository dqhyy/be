package com.huydo.be.controller;

import com.huydo.be.dto.request.ApiResponse;
import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.dto.response.LoginResponse;
import com.huydo.be.entity.Account;
import com.huydo.be.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/register")
    ApiResponse<Account> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.<Account>builder()
                .result(accountService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse result = accountService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/avatar")
    public ApiResponse<String> uploadAvatar(
            @RequestParam("file") MultipartFile file) {
        accountService.uploadAvatar(file);
        return ApiResponse.<String>builder()
                .result("Avatar uploaded successfully")
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<Account> getMyInfo() {
        return ApiResponse.<Account>builder()
                .result(accountService.getMyInfo())
                .build();
    }

    @PutMapping("/my-info")
    public ApiResponse<Account> updateMyInfo(@RequestBody com.huydo.be.dto.request.AccountUpdateRequest request) {
        return ApiResponse.<Account>builder()
                .result(accountService.updateMyInfo(request))
                .build();
    }
}
