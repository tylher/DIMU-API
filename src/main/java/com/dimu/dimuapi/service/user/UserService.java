package com.dimu.dimuapi.service.user;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.EditProfileDto;
import com.dimu.dimuapi.dto.OnboardDto;
import com.dimu.dimuapi.dto.SignupDto;
import com.dimu.dimuapi.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public ApiResponseDto registerUser(SignupDto signupDto) throws Exception;

    public ApiResponseDto onBoardUser(OnboardDto onBoardDto, User user) throws Exception;

    String resetPassword(String email, String password,String code);

    public ApiResponseDto resetPasswordAfterLogin(String email, String password);

    public ApiResponseDto editProfile(EditProfileDto editProfileDto,String userId);

    ApiResponseDto updateProfileImage(MultipartFile file, String userId);
}
