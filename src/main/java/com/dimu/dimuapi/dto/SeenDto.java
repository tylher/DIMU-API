package com.dimu.dimuapi.dto;

import java.time.LocalDateTime;

public record SeenDto(
        LocalDateTime lastReadAt,
        String conversationId,
        String userId
) {
}
