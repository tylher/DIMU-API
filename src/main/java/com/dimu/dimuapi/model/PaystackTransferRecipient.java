package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaystackTransferRecipient {
    private boolean status;
    private String message;
    private PaystackData data;

    @Data
    public static class PaystackData {
        private int integration;
        private String domain;
        private String currency;
        private String name;
        private String recipient_code;
        private int id;
        private boolean active;
        private String type;
        private boolean is_deleted;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date updatedAt;

        private Details details;
    }

    @Data
    public static class Details {
        private String authorization_code;
        private String account_name;
        private String account_number;
        private String bank_code;
        private String bank_name;
    }
}
