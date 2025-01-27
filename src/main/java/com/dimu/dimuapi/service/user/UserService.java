package com.dimu.dimuapi.service.user;

import com.dimu.dimuapi.dto.SignupDto;

public interface UserService {
    public String registerUser(SignupDto signupDto) throws Exception;
}
