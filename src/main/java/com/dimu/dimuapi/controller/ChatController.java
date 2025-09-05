package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.MessageDto;
import com.dimu.dimuapi.dto.MessageStatusDto;
import com.dimu.dimuapi.dto.SeenDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.chatmessage.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
   @Autowired
   ChatMessageService chatMessageService;


    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponseDto> chat(@PathVariable String conversationId){
        ApiResponseDto response = chatMessageService.getChatMessages(conversationId);
        return ResponseEntity.ok(response);
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto message) throws Exception{
        chatMessageService.sendChatMessage(message);
    }

    @MessageMapping("/chat.readMessage")
    public void readMessage(@Payload SeenDto seenDto) throws Exception{
        chatMessageService.markMessagesAsRead(seenDto);
    }

    @MessageMapping("/chat.changeMessageStatus")
    public void changeMessageStatus(@Payload MessageStatusDto messageStatusDto) throws Exception{
        chatMessageService.changeMessageStatus(messageStatusDto);
    }
}

