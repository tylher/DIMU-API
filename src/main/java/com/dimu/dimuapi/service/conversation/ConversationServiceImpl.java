package com.dimu.dimuapi.service.conversation;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.Conversation;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.ConversationRepository;
import com.dimu.dimuapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService{
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ApiResponseDto getConversation(User user, String partnerId) {
         try {
             Conversation conversation = conversationRepository.getConversationByParticipants(user.getUserId(), partnerId)
                     .orElseGet(() -> createConversation(user, partnerId));
             return new ApiResponseDto(true,"Conversations fetched successfully"
                     ,conversationRepository.save(conversation));
         } catch (Exception e) {
             throw new CustomException(e.getMessage());
         }
    }


    private Conversation createConversation(User userA, String userBId) {
        User userB = userRepository.findById(userBId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userBId)
        );
        return Conversation.builder()
                .participants(List.of(userA,userB))
                .build();
    }
}
