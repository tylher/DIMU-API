package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DiimuTokenController {
    @Autowired
    @Qualifier("DiimuVerificationTokenService")
    DiimuTokenService diimuTokenService;

    @GetMapping("/verification-token/resend")
    ResponseEntity<ApiResponseDto> resendverificationToken(@AuthenticationPrincipal User user) throws Exception {
        String response = diimuTokenService.resendToken(user.getEmail());
        return new ResponseEntity<>(new ApiResponseDto(true, response), HttpStatus.OK);
    }
}
