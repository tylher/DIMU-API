package com.dimu.dimuapi.service.dispute;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateDisputeDto;
import com.dimu.dimuapi.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DisputeService {
    public ApiResponseDto createDispute(CreateDisputeDto disputeDto
            , List<MultipartFile> productImageFiles, User user);

    public ApiResponseDto getDisputeByAgreement(String agreementId);
}
