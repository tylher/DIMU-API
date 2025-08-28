package com.dimu.dimuapi.dto;

import jakarta.validation.constraints.Pattern;

public record MessageStatusDto(
    String conversationId,
    String messageId,
    @Pattern(regexp = "^(SENT|DELIVERED)")
    String status,
    String userId
) {
}
