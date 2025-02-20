package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class DiimuTokenController {
    @Autowired
    @Qualifier("DiimuVerificationTokenService")
    DiimuTokenService diimuTokenService;

    @GetMapping("/verification-token/send")
    ResponseEntity<ApiResponseDto> resendverificationToken(@RequestParam String email) throws Exception {
        String response = diimuTokenService.sendToken(email);
        return new ResponseEntity<>(new ApiResponseDto(true, response), HttpStatus.OK);
    }
}
