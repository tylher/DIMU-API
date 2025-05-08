package com.dimu.dimuapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PaystackSubaccount {
    private boolean status;
    private String message;
    private Data data;

    @lombok.Data
    public class Data {
        private String business_name;
        private String account_number;
        private Integer percentage_charge;
        private String settlement_bank;
        private String currency;
        private Integer bank;
        private Integer integration;
        private String domain;
        private String account_name;
        private String product;
        private Integer managed_by_integration;
        private String subaccount_code;
        private boolean is_verified;
        private String settlement_schedule;
        private boolean active;
        private boolean migrate;
        private Integer id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
