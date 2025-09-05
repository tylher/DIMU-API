package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.Conversation;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/conversation")
@RequiredArgsConstructor
public class ConversationController {
    private  final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<ApiResponseDto> getUserConversations(@AuthenticationPrincipal User user){
        ApiResponseDto result = conversationService.getAllConversations(user);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<ApiResponseDto> getSingleConversation(@AuthenticationPrincipal User user, @PathVariable String partnerId){
        ApiResponseDto result = conversationService.getConversation(user,partnerId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
