package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record EditAgreementDto(
        String itemName,

        @PositiveOrZero(message = "price should be greater than zero")
        Double price,

        Double upfrontPayment,

        @Pattern(regexp = "^(GOODS_SERVICES|ELECTRONICS|FASHION|FOOD|DIGITAL_PRODUCTS)$",
                message = "category should be one of the following: GOODS_SERVICES, ELECTRONICS, FASHION, FOOD, DIGITAL_PRODUCTS")
        String category,

        String deliveryAddress,

        String paidBy,

        Integer inspectionPeriod,

        String sellerEmail,

        String sellerPhoneNumber
) {
}
