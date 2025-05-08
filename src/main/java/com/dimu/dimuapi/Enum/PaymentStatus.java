package com.dimu.dimuapi.Enum;

import lombok.Getter;

@Getter
public enum PaymentStatus {
        PENDING_INITIATION("pending_initiation"),
        INITIATED("pending"),
        ABANDONED("abandoned"),
        COMPLETED("success"),
        REVERSED("reversed"),
        REJECTED("rejected"),
        RECEIVED("received"),
        FAILED("failed");

        private final String status;

        PaymentStatus(String status){
            this.status = status;
        }
    }
