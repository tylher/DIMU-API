package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AgreementPaymentDto(
        @NotEmpty(message = "Payment type cannot be empty")
        @Pattern(regexp = "^(ONLINE|WALLET)$")
        String paymentType,


        String walletId
) {
}
