package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CreateGoodServiceDto(
        @NotEmpty(message = "Category should not be empty")
        @Pattern(regexp = "(GOODS_SERVICES|ELECTRONICS|FASHION|FOOD|DIGITAL_PRODUCTS)",
                message = "Category should be one of the following: GOODS_SERVICES, ELECTRONICS, FASHION, FOOD, DIGITAL_PRODUCTS")
        String category,

        @NotEmpty(message = "Item condition should not be empty")
        @Pattern(regexp = "^(NEW|FAIRLY_USED|LONDON_USED)$",
                message = "Item condition  should be one of the following: NEW, FAIRLY_USED, LONDON_USED")
        String itemCondition,


        String proofOfAuthenticationExists,

        @NotEmpty(message = "Delivery method should not be empty")
        @Pattern(regexp = "^(PICK_UP|DOOR_DELIVERY)$",
                message = "Delivery method should be one of the following: PICK_UP, DOOR_DELIVERY")
        String deliveryMethod,

        boolean additionalItems

) {
}
