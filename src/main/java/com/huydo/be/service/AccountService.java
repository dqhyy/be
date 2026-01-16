package com.huydo.be.service;

import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;
import com.huydo.be.entity.Account;

public interface AccountService {
    Account register(RegisterRequest request);
    String login(LoginRequest request);
}

