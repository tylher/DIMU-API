package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record InitiateTransferDto(
        @NotEmpty(message = "Source should not be empty")
        String source,
        String reason,
        @Positive(message = "amount should be greater than zero")
        int amount,
        @NotEmpty(message = "recipient should not be empty")
        String recipient
) {
}
