package com.dimu.dimuapi.dto;

import com.dimu.dimuapi.model.User;

public record LoginResponseDto(
        String jwtToken,
        User user
) {
}
