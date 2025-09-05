package com.dimu.dimuapi.Enum;

public enum ComplaintType {
    DAMAGED_GOODS("Damaged Goods"),
    MISSING_GOODS("Missing Goods"),
    EXPIRED_GOODS("Expired Goods"),
    INCOMPLETE_PRODUCT("Incomplete Product"),
    BURNT_GOODS("Burnt Goods"),
    OTHER("Other");

    private final String value;

    ComplaintType(String value) {
        this.value = value;
    }
}
