package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DecideAgreementDto(
        @NotEmpty(message = "Agreement id cannot be empty")
        String agreementId,

        @NotNull(message=" isAccepted cannot be null")
        boolean isAccepted
) {
}
