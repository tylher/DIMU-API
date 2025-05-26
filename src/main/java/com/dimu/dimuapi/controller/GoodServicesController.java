package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateGoodServiceDto;
import com.dimu.dimuapi.dto.UpdateDeliveryStatusDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.goodservice.GoodServiceService;
import com.dimu.dimuapi.util.DiimuUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/api/good-services")
public class GoodServicesController {
    @Autowired
    GoodServiceService goodServiceService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createGoodServices(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody CreateGoodServiceDto createGoodServiceDto

                                               ){

        try{

            ApiResponseDto apiResponseDto = goodServiceService.createGoodService(createGoodServiceDto);

            return ResponseEntity.ok(apiResponseDto);


        } catch (ConstraintViolationException ex) {
            log.error("error converting and validating json {}", ex.getMessage());
            throw new CustomException("error converting and validating json " + ex.getMessage());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto> updateGoodServices(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody UpdateDeliveryStatusDto updateDeliveryStatusDto

    ){

        ApiResponseDto apiResponseDto = goodServiceService.updateDeliveryStatus(updateDeliveryStatusDto.goodServicesId()
                ,updateDeliveryStatusDto.deliveryStatus());
        return ResponseEntity.ok(apiResponseDto);
    }

}
