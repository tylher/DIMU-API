package com.dimu.dimuapi.service.agreement;

import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.EditAgreementDto;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.User;

public interface AgreementService {
    public ApiResponseDto createAgreementByBuyer(AgreementDto agreementDto, User user);
    public ApiResponseDto getAgreementsByUser(User user);
    public ApiResponseDto getAgreement(User user, String agreementId);
    public ApiResponseDto editAgreement(User user, String agreementId, EditAgreementDto agreementDto);
    public ApiResponseDto payForAgreement( String transactionId);
    public String acceptOrDeclineAgreement(String agreementId, boolean isAccepted, User user);
}
