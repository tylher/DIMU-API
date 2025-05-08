package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaystackInitiateTransferResponse {
    private boolean status;
    private String message;
    private PaystackData data;

    @Data
    public static class PaystackData {
        private int integration;
        private String domain;
        private int amount;
        private String currency;
        private String source;
        private String reason;
        private int recipient;
        private String status;
        private String transfer_code;
        private int id;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date updatedAt;
    }
}
