package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;

public record FundWalletDto(
        @NotEmpty(message = "wallet id cannot be empty")
        String walletId,
        String reference
) {
}
