package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PaystackInitializeRequest(
        @NotEmpty(message = "Email should not be empty")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
        String email,
        @NotEmpty(message = "Amount should not be empty")
        @Positive(message = "Amount should be greater than zero")
        String amount,
        String reference
) {
}
