package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.PasswordResetDto;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import com.dimu.dimuapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    @Qualifier("DiimuPasswordTokenService")
    private DiimuTokenService diimuTokenService;

    @Autowired
    private UserService userService;

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponseDto> resetPassword(@RequestBody PasswordResetDto passwordResetDto
            ,@RequestParam String code){
        String response = userService.resetPassword(passwordResetDto.email(), passwordResetDto.password(), code);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true
                ,response);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @PostMapping("/password-token/verify")
    public  ResponseEntity<ApiResponseDto> verifyToken(@RequestParam String code,@RequestParam String email){
        String response = diimuTokenService.verifyToken(email,code);
        return new ResponseEntity<>(new ApiResponseDto(true,response), HttpStatus.OK);
    }

    @GetMapping("/password-token/send")
    ResponseEntity<ApiResponseDto> resendPasswordResetToken(@RequestParam String email) throws Exception {
        String response = diimuTokenService.sendToken(email);
        return new ResponseEntity<>(new ApiResponseDto(true, response), HttpStatus.OK);
    }
}
