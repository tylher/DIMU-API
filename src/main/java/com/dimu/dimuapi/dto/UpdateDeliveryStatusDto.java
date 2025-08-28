package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateDeliveryStatusDto(
        String goodServicesId,

        @Pattern(regexp = "^(PROCESSING|IN_TRANSIT|DELIVERED)")
        String deliveryStatus,

        String riderNumber

) {
}
