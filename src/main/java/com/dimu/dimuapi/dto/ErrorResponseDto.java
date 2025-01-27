package com.dimu.dimuapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponseDto{
    private String errorMessage;
    private HttpStatus errorCode;
    private String apiPath;
    private LocalDateTime errorTime;
}
