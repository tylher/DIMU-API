package com.dimu.dimuapi.service.agreement;

import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.EditAgreementDto;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AgreementService {
    public ApiResponseDto createNewAgreement(AgreementDto agreementDto, User user,String initiatedBy, Optional<MultipartFile> poaFile);
    public ApiResponseDto getAgreementsByUser(User user);
    public ApiResponseDto getAgreement(User user, String agreementId);
    public ApiResponseDto editAgreement(User user, String agreementId, EditAgreementDto agreementDto);
    public ApiResponseDto payForAgreement( String transactionId,String paymentType, String walletId,User user);
    public ApiResponseDto acceptOrDeclineAgreement(String agreementId, boolean isAccepted, User user);
}
