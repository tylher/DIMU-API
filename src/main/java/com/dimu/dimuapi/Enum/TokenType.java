package com.dimu.dimuapi.Enum;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TokenType {
    VERIFICATION(4,5),
    RESET_PASSWORD(4,10),
    TRANSACTION_ID(8,Integer.MAX_VALUE);

    private final int length;
    private final int expiry;

    TokenType(int length,int expiry){
        this.length = length;
        this.expiry= expiry;
    }
}
