package com.huydo.be.service;

import com.huydo.be.dto.request.AccountUpdateRequest;
import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.dto.response.LoginResponse;
import com.huydo.be.entity.Account;

public interface AccountService {
    Account register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void uploadAvatar(org.springframework.web.multipart.MultipartFile file);

    Account getMyInfo();

    Account updateMyInfo(AccountUpdateRequest request);
}
