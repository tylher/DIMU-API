package com.dimu.dimuapi.model;

import lombok.Data;

@Data
public class PaystackVerifyAccount {
    private boolean status;
    private String message;
    private PaystackData data;

    @Data
    public static class PaystackData {
        private String account_number;
        private String account_name;
    }
}
