package com.dimu.dimuapi.service.chatmessage;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.MessageDto;
import com.dimu.dimuapi.dto.MessageStatusDto;
import com.dimu.dimuapi.dto.SeenDto;
import com.dimu.dimuapi.model.User;
import jakarta.mail.Message;

public interface ChatMessageService {
    public void sendChatMessage(MessageDto messageDto) throws Exception;

    public void changeMessageStatus(MessageStatusDto messageStatusDto);

    public void markMessagesAsRead(SeenDto seenDto);

    public ApiResponseDto getChatMessages(String conversationId);
}
