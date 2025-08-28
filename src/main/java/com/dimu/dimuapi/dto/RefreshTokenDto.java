package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenDto(
        @NotEmpty(message = "Token cannot be empty")
        String token
) {
}
