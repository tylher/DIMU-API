package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateDisputeDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.dispute.DisputeService;
import com.dimu.dimuapi.util.DiimuUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/dispute")
public class DisputeController {
    @Autowired
    DisputeService disputeService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createDispute(@AuthenticationPrincipal User user
, @RequestPart(name = "productImageFiles",required = false) List<MultipartFile> productImageFiles, @RequestPart String complaintType
            , @RequestPart(name = "complaint",required = false ) String complaint, @RequestPart String agreementId){ {

    CreateDisputeDto disputeDto = new CreateDisputeDto(agreementId,complaintType,complaint);
    DiimuUtils.validateInput(disputeDto);


                return new ResponseEntity<>(disputeService.createDispute(disputeDto,productImageFiles,user), HttpStatus.OK);
    }
    }


    @GetMapping("/get/{agreementId}")
    public ResponseEntity<ApiResponseDto> getDispute(@PathVariable String agreementId){
        return new ResponseEntity<>(disputeService.getDisputeByAgreement(agreementId),HttpStatus.OK);
    }


}
