package com.huydo.be.service;

import com.huydo.be.dto.request.LoginRequest;
import com.huydo.be.dto.request.RegisterRequest;

public interface AccountService {
    void register(RegisterRequest request);
    String login(LoginRequest request);

}

