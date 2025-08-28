package com.dimu.dimuapi.service.conversation;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;

public interface ConversationService {
    public ApiResponseDto getConversation(User user, String partnerId);
}
