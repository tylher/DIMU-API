package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.*;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.agreement.AgreementService;
import com.dimu.dimuapi.util.DiimuUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/agreement")
public class AgreementController {
    @Autowired
    AgreementService agreementService;

    @PostMapping("buyer/create")
    public ResponseEntity<ApiResponseDto> createAgreementByBuyer(@Valid @RequestBody AgreementDto agreementDto, @AuthenticationPrincipal User
                                                          user){
        ApiResponseDto result = agreementService.createNewAgreement(agreementDto,user,"buyer",Optional.empty(),null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("seller/create")
    public ResponseEntity<ApiResponseDto> createAgreementBySeller( @AuthenticationPrincipal User
            user,@RequestPart(value = "itemName") String itemName, @RequestPart(value = "price") String price
            ,@RequestPart(value = "upfrontPayment") String upfrontPayment, @RequestPart(value = "category") String category
            ,@RequestPart(value = "deliveryAddress", required = false) String deliveryAddress
            ,@RequestPart(value = "inspectionPeriod", required = false) String inspectionPeriod
            ,@RequestPart(value = "sellerEmail", required = false) String sellerEmail
            ,@RequestPart(value = "sellerPhoneNumber", required = false) String sellerPhoneNumber
            ,@RequestPart(value = "buyerEmail", required = false) String buyerEmail
            ,@RequestPart(value = "buyerPhoneNumber", required = false) String buyerPhoneNumber
            ,@RequestPart(value = "paymentType") String paymentType
            ,@RequestPart(value = "goodServiceId") String goodServiceId
            ,@RequestPart(value = "proofOfAuthenticity",required = false) Optional<MultipartFile> poaFile
            ,@RequestPart(value = "additionalFiles",required = false) List<MultipartFile> additionalFiles
           ){

        AgreementDto agreementDto = new AgreementDto(itemName,Double.parseDouble(price)
                ,upfrontPayment!=null?Double.parseDouble(upfrontPayment):null,category,deliveryAddress
                ,inspectionPeriod!=null?Integer.parseInt(inspectionPeriod):null,sellerEmail,sellerPhoneNumber,buyerEmail,buyerPhoneNumber,goodServiceId,paymentType);

        DiimuUtils.validateInput(agreementDto);

        ApiResponseDto result = agreementService.createNewAgreement(agreementDto,user,"seller",poaFile,additionalFiles);
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

    @PostMapping("/pay/{transactionId}")
    public ResponseEntity<ApiResponseDto> payForAgreement(@PathVariable String transactionId, @RequestBody @Valid AgreementPaymentDto paymentDto, @AuthenticationPrincipal User user){
        ApiResponseDto result = agreementService.payForAgreement(transactionId,paymentDto.paymentType(), paymentDto.walletId(),user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/make-choice")
    public ResponseEntity<ApiResponseDto> acceptOrDeclineAgreement(
            @AuthenticationPrincipal User user, @Valid @RequestBody DecideAgreementDto dto){
        ApiResponseDto responseDto = agreementService.acceptOrDeclineAgreement(dto.agreementId(), dto.initiatedBy(),
                dto.isAccepted(), user);

        return ResponseEntity.ok(responseDto);
    }


}
