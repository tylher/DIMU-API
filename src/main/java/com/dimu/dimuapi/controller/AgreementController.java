package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.EditAgreementDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.agreement.AgreementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/agreement")
public class AgreementController {
    @Autowired
    AgreementService agreementService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createAgreement(@Valid @RequestBody AgreementDto agreementDto, @AuthenticationPrincipal User
                                                          user){
        ApiResponseDto result = agreementService.createAgreementByBuyer(agreementDto,user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto> getAllAgreements(@AuthenticationPrincipal User user){
        ApiResponseDto result = agreementService.getAgreementsByUser(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{agreementId}")
    public ResponseEntity<ApiResponseDto> getAgreement(@PathVariable String agreementId, @AuthenticationPrincipal User user){
        ApiResponseDto result = agreementService.getAgreement(user,agreementId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/edit/{agreementId}")
    public ResponseEntity<ApiResponseDto> updateAgreement(@PathVariable String agreementId,
                                                          @Valid @RequestBody EditAgreementDto editAgreementDto,
                                                          @AuthenticationPrincipal User user){
        ApiResponseDto result = agreementService.editAgreement(user,agreementId,editAgreementDto);
        return ResponseEntity.ok(result);
    }
}
