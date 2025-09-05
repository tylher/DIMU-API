package com.dimu.dimuapi.service.conversation;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.Conversation;
import com.dimu.dimuapi.model.ConversationParticipants;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.ConversationRepository;
import com.dimu.dimuapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService{
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    UserRepository userRepository;

    private final Sort sort = Sort.by(Sort.Direction.DESC,"updatedAt");

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


    private Conversation createConversation(User user, String partnerId) {
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", partnerId));

        Conversation conversation = new Conversation();
        conversation.setLastMessage(null);
        conversation.setLastMessageTime(null);

        ConversationParticipants participant1 = new ConversationParticipants();
        participant1.setConversation(conversation);
        participant1.setUser(user);
        participant1.setUnreadCount(0);

        ConversationParticipants participant2 = new ConversationParticipants();
        participant2.setConversation(conversation);
        participant2.setUser(partner);
        participant2.setUnreadCount(0);

        conversation.setParticipants(List.of(participant1, participant2));

        return conversation;
    }

    @Override
    public ApiResponseDto getAllConversations(User user){
        List<Conversation> conversations=  conversationRepository
                .findDistinctByUserIdOrderByUpdatedAtDesc(user.getUserId());
        return new ApiResponseDto(true,"All conversations fetched successfully",conversations);
    }
}
