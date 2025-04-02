package com.dimu.dimuapi.service.auth;

import com.dimu.dimuapi.dto.LoginResponseDto;

public interface AuthService {
    public LoginResponseDto login(String username, String password);
}
