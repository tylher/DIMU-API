package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record SecurePinDto(
        @NotEmpty(message = "Secure Pin should not be empty")
        @Pattern(regexp = "^\\d{4}$", message = "Secure Pin should be 4 digits")
        String securePin
) {
}
