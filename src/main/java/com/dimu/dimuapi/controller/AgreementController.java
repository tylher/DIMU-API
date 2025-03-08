package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.agreement.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agreement")
public class AgreementController {
    @Autowired
    AgreementService agreementService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createAgreement(@AuthenticationPrincipal User
                                                          user, AgreementDto agreementDto){
        ApiResponseDto result = agreementService.createAgreementByBuyer(agreementDto,user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto> getAllAgreements(@AuthenticationPrincipal User user){
        ApiResponseDto result = agreementService.getAgreementsByUser(user);
        return ResponseEntity.ok(result);
    }
}
