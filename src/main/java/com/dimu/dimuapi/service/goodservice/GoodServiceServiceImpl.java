package com.dimu.dimuapi.service.goodservice;

import com.dimu.dimuapi.Enum.AWSBucketList;
import com.dimu.dimuapi.Enum.DeliveryMethod;
import com.dimu.dimuapi.Enum.ItemCategory;
import com.dimu.dimuapi.Enum.ItemCondition;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateGoodServiceDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.GoodServices;
import com.dimu.dimuapi.repository.GoodServicesRepository;
import com.dimu.dimuapi.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GoodServiceServiceImpl implements GoodServiceService{

    @Autowired
    S3Service s3Service;

    @Autowired
    GoodServicesRepository goodServicesRepository;

    @Override
    public ApiResponseDto createGoodService(CreateGoodServiceDto createGoodServiceDto){

        try{
            GoodServices goodServices = new GoodServices();


            goodServices.setDeliveryMethod(DeliveryMethod.valueOf(createGoodServiceDto.deliveryMethod()));
            goodServices.setItemCondition(ItemCondition.valueOf(createGoodServiceDto.itemCondition()));
            goodServices.setCategory(ItemCategory.valueOf(createGoodServiceDto.category()));
            goodServices.setProofOfAuthenticationExists(createGoodServiceDto.proofOfAuthenticationExists() != null && Boolean.parseBoolean(createGoodServiceDto.proofOfAuthenticationExists()));
            return new ApiResponseDto(true,"Good or service created successfully",goodServicesRepository.save(goodServices));
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new CustomException("Error creating good or service: " + ex.getMessage());
        }
    }
}
