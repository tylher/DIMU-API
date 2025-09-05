package com.dimu.dimuapi.dto;

public record MessageDto(
        String content,
        String senderId,
        String conversationId

) {
}
