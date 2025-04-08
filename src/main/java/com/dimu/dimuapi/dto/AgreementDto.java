package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.*;

public record AgreementDto(
        @NotEmpty(message = "Item name should not be empty")
        String itemName,

        @NotNull(message = "Item description should not be null")
        @PositiveOrZero(message = "price should be greater than zero")
        Double price,

        Double upfrontPayment,

        @NotEmpty(message = "category should not be empty")
        @Pattern(regexp = "(GOODS_SERVICES|ELECTRONICS|FASHION|FOOD|DIGITAL_PRODUCTS)",
                message = "category should be one of the following: GOODS_SERVICES, ELECTRONICS, FASHION, FOOD, DIGITAL_PRODUCTS")
        String category,

        String deliveryAddress,

        String paidBy,

        Integer inspectionPeriod,

        @NotEmpty(message = "seller's email should not be empty")
        String sellerEmail,

        @NotEmpty(message = "seller's phone number should not be empty")
        String sellerPhoneNumber,

        @NotEmpty(message = "payment type should not be empty")
        @Pattern(regexp = "(WALLET|ONLINE)", message = "payment type should be one of the following: WALLET, ONLINE")
        String paymentType
) {
}
