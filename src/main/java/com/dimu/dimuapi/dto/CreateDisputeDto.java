package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CreateDisputeDto(
    @NotEmpty(message = "Agreement ID should not be empty")
    String agreementId,

    @Pattern(message = "Complaint type must be either ' DAMAGED_GOODS', ' MISSING_GOODS', ' EXPIRED_GOODS', ' INCOMPLETE_PRODUCT', ' BURNT_GOODS', ' OTHER'."
           , regexp = "^(DAMAGED_GOODS|MISSING_GOODS|EXPIRED_GOODS|INCOMPLETE_PRODUCT|BURNT_GOODS|OTHER)$")
    String complaintType,

    String complaint

) {
}
