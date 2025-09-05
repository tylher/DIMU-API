package com.dimu.dimuapi.Enum;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TokenType {
    VERIFICATION(4,5),
    RESET_PASSWORD(4,10),
    RESET_SECURE_PIN(6,10),
    TRANSACTION_ID(8,Integer.MAX_VALUE),
    REFRESH(32,60 * 24 * 7);

    private final int length;
    private final int expiry;

    TokenType(int length,int expiry){
        this.length = length;
        this.expiry= expiry;
    }
}
