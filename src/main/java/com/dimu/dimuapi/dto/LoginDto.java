package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record LoginDto(
        @NotEmpty(message = "Email cannot be empty ")
        @Email(message = "Email should be valid")
        String email,

        @NotEmpty(message="Password cannot be empty")
        String password
) {
}
