package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.LoginDto;
import com.dimu.dimuapi.dto.LoginResponseDto;
import com.dimu.dimuapi.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        LoginResponseDto loginResponseDto = authService.login(loginDto.email(), loginDto.password());
        return new ResponseEntity<>(new ApiResponseDto(true,"Login successful", loginResponseDto )
                , HttpStatus.OK);
    }
}
