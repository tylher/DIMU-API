package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import com.dimu.dimuapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/password")
public class PasswordController {
    @Autowired
    @Qualifier("DiimuPasswordTokenService")
    private DiimuTokenService diimuTokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-password-token")
    public ResponseEntity<String> sendPasswordToken( @RequestParam String email){
        String response = diimuTokenService.createToken(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-password-token")
    public ResponseEntity<String> verifyPasswordToken(@RequestParam String email
            , @RequestParam String token){
        String response = diimuTokenService.verifyToken(email,token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto> resetPassword(@RequestParam String email,
             @RequestParam String password){
        String response = userService.resetPassword(email,password);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true
                ,response);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}
