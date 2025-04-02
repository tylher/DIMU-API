package com.dimu.dimuapi.service.user;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.OnboardDto;
import com.dimu.dimuapi.dto.SignupDto;
import com.dimu.dimuapi.model.User;

public interface UserService {
    public ApiResponseDto registerUser(SignupDto signupDto) throws Exception;

    public ApiResponseDto onBoardUser(OnboardDto onBoardDto, User user) throws Exception;

    String resetPassword(String email, String password,String code);
}
