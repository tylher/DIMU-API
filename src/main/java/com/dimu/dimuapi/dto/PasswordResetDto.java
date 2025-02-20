package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;

public record PasswordResetDto(
        @NotEmpty(message = "Email should not be empty")
        String email,

        @NotEmpty(message = "Password should not be empty")
        String password
) {
}
