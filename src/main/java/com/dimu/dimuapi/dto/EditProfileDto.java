package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record EditProfileDto(
        String firstName,

        String lastName,
        String gender,
        @Pattern(
                regexp = "^\\+?(\\d{1,3})?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$",
                message = "Please enter a valid phone number"
        )
        String phoneNumber,

        LocalDate dateOfBirth,

        String country,
        String countryCode,

        String state

) {
}
