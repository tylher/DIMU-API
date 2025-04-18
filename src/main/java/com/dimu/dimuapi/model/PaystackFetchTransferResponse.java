package com.dimu.dimuapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaystackFetchTransferResponse {
    private String status;
    private String message;
    private PaystackData data;

    @Data
    @NoArgsConstructor
    public static class PaystackData {
        private int id;
        private String source;
        private int amount;
        private String currency;
        private String domain;
        private int integration;
        private String status;
        private String reason;
        private String reference;
        private String createdAt;
        private String transfer_code;
        private int request;
        private String updatedAt;
        private Recipient recipient;
        private int fee_charged;
    }

    @Data
    @NoArgsConstructor
    public static class Recipient {
        private String name;
        private boolean active;
        private String currency;
        private String domain;
        private String email;
        private String recipient_code;
        private int integration;
        private String createdAt;
        private int id;
        private String updatedAt;
        private  String type;
        private boolean isDeleted;
        private boolean is_deleted;
        private RecipientDetail details;
    }

    @Data
    @NoArgsConstructor
    public static class RecipientDetail {
        private String account_number;
        private String account_name;
        private String bank_code;
        private String bank_name;
        private String authorization_code;
    }
}
