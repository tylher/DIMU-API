package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;

public record PaystackCreateTransferRecipientDto(
        @NotEmpty(message = "Type should not be empty")
        String type,
        @NotEmpty(message = "Name should not be empty")
        String name,
        @NotEmpty(message = "Account number should not be empty")
        String account_number,
        @NotEmpty(message = "Bank code should not be empty")
        String bank_code,
        @NotEmpty(message = "Currency should not be empty")
        String currency
) {
}
