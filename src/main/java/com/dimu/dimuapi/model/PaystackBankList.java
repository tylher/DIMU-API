package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PaystackBankList {
    private boolean status;
    private String message;
    private List<PaystackData> data;

    @Data
    public static class PaystackData {
        private String name;
        private String slug;
        private String code;
        private String longcode;
        private String gateway;
        private boolean is_deleted;
        private  boolean pay_with_bank;
        private String currency;
        private String country;
        private String type;
        private int id;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date updatedAt;
    }
}
