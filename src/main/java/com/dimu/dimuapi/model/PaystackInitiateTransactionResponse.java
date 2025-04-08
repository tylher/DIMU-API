package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaystackInitiateTransactionResponse {
    private boolean status;
    private String message;
    private PaystackData data;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaystackData {
        @JsonProperty("authorization_url")
        private String authorizationUrl;

        @JsonProperty("access_code")
        private String accessCode;

        private String reference;
    }
}
