package com.dimu.dimuapi.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponseDto {
    private boolean status;
    private Object data;
    private String message;

    public ApiResponseDto(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

}
