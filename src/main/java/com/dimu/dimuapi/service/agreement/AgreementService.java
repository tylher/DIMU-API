package com.dimu.dimuapi.service.agreement;

import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.User;

public interface AgreementService {
    public ApiResponseDto createAgreementByBuyer(AgreementDto agreementDto, User user);
    public ApiResponseDto getAgreementsByUser(User user);
}
