package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record AgreementDto(
        @NotEmpty(message = "Item name should not be empty")
        String itemName,
        @NotEmpty(message = "Item description should not be empty")
        @PositiveOrZero(message = "price should be greater than zero")
        double price,
        double upfrontPayment,
        @NotEmpty(message = "category should not be empty")
        @Pattern(regexp = "(GOODS_SERVICES|ELECTRONICS|FASHION|FOOD|DIGITAL_PRODUCTS)",
                message = "category should be one of the following: GOODS_SERVICES, ELECTRONICS, FASHION, FOOD, DIGITAL_PRODUCTS")
        String category,
        String deliveryAddress,
        String paidBy,
        int inspectionPeriod,
        @NotEmpty(message = "seller's email should not be empty")
        String sellerEmail,
        @NotEmpty(message = "seller's phone number should not be empty")
        String sellerPhoneNumber
) {
}
