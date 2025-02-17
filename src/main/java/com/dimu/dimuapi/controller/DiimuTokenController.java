package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.service.token.DiimuTokenService;
import org.springframework.beans.factory.annotation.Autowired;
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
    DiimuTokenService diimuTokenService;

    @GetMapping("/verification-token/resend")
    ResponseEntity<String> resendverificationToken(@AuthenticationPrincipal String email) throws Exception {
        String response = diimuTokenService.resendVerificationToken(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
