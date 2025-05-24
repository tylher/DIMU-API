package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/securepin")
public class SecurePinController {
    @Autowired
    @Qualifier("DiimuSecurePinTokenService")
    DiimuTokenService diimuTokenService;

    @GetMapping("/send-token")
    public ResponseEntity<ApiResponseDto> sendSecurePinResetToken(@RequestParam String email) throws Exception {
        String response = diimuTokenService.sendToken(email);
        return new ResponseEntity<>(new ApiResponseDto(true, response), HttpStatus.OK);
    }

    @GetMapping("verify-token")
    public ResponseEntity<ApiResponseDto>  verifySecurePinResetToken(@RequestParam String code, @RequestParam String email) throws Exception {
        String response = diimuTokenService.verifyToken(email, code);
        return new ResponseEntity<>(new ApiResponseDto(true, response), HttpStatus.OK);
    }



}
