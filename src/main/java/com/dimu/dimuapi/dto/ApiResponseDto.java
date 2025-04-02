package com.dimu.dimuapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponseDto {
    private boolean status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;


    public ApiResponseDto(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

}
