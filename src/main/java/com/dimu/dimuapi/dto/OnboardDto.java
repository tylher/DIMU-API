package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record OnboardDto(
    @NotEmpty(message = "firstName should not be empty")
    String firstName,
    @NotEmpty(message = "lastName should not be empty")
    String lastName,
    String gender,
    @NotEmpty(message = "phoneNumber should not be empty")
    @Pattern(
            regexp = "^\\+?(\\d{1,3})?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$",
            message = "Please enter a valid phone number"
    )
    String phoneNumber,

    LocalDate dateOfBirth,
    @NotEmpty(message = "country should not be empty")
    String country,
    @NotEmpty(message = "state should not be empty")
    String state

) {
}
