package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MakeWithdrawalDto(
    @NotEmpty(message = "Secure pin should not be empty")
    @Pattern(regexp = "^\\d{4}$", message = "Secure Pin should be 4 digits")
    String securePin,

    @NotEmpty(message = "Wallet id cannot be empty")
    String walletId,

    @Positive(message = "Amount should be greater than zero")
    @NotNull(message = "Amount cannot be null")
    int amount,

    @NotEmpty(message = "Source should not be empty")
    String source,

    String reason,

    @NotEmpty(message = "recipient should not be empty")
    String recipient
) {
}
