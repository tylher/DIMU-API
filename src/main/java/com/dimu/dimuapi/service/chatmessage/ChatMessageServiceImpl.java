package com.dimu.dimuapi.service.chatmessage;

import com.dimu.dimuapi.Enum.MessageStatus;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.MessageDto;
import com.dimu.dimuapi.dto.MessageStatusDto;
import com.dimu.dimuapi.dto.SeenDto;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.ChatMessage;
import com.dimu.dimuapi.model.Conversation;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.ChatMessageRepository;
import com.dimu.dimuapi.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class ChatMessageServiceImpl implements ChatMessageService{
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendChatMessage(MessageDto messageDto) throws Exception {
        try{
            Conversation conversation = getConversationById(messageDto.conversationId());

            User user = conversation.getParticipants().stream()
                    .filter(p -> p.getUserId().equals(messageDto.senderId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", messageDto.senderId()));

            ChatMessage chatMessage = ChatMessage.builder()
                    .sender(user)
                    .content(messageDto.content())
                    .conversation(conversation)
                    .createdAt(Instant.now())
                    .build();

            chatMessageRepository.save(chatMessage);

            conversation.setLastMessage(chatMessage.getContent());
            conversation.setLastMessageTime(chatMessage.getCreatedAt());
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);

            conversationRepository.save(conversation);

            messagingTemplate.convertAndSend("/topic/messages/"+messageDto.conversationId(), chatMessage);
        }catch (ResourceNotFoundException ex){
            throw ex;
        }

        catch (Exception ex){
            log.error("An unexpected error occurred, "+ex.getMessage());
            throw new Exception("An unexpected error occurred");
        }

    }


    @Override
    public void changeMessageStatus(MessageStatusDto messageStatusDto) {
        //TODO: implement changing message status
        ChatMessage message = getChatMessage(messageStatusDto.messageId());
        Conversation conversation = getConversationById(messageStatusDto.conversationId());

        if(message.getStatus().equals(MessageStatus.READ)){
            return;
        }
        // Prevent sender from marking their own message as delivered or read
        if(message.getStatus().equals(MessageStatus.DELIVERED) && message.getSender().getUserId()
                .equals(messageStatusDto.userId())){
            return;
        }

    if(MessageStatus.valueOf(messageStatusDto.status()).ordinal()>message.getStatus().ordinal()){
        message.setStatus(MessageStatus.valueOf(messageStatusDto.status()));

        chatMessageRepository.save(message);

        messagingTemplate.convertAndSendToUser(message.getSender().getUserId(), "/queue/messages", new MessageStatusDto(messageStatusDto.messageId()
                , message.getStatus().toString(), null, messageStatusDto.conversationId()));
    }

    }

    @Override
    public void markMessagesAsRead(SeenDto seenDto) {
        List<ChatMessage> chatMessages = getChatMessagesAfterLastSeenMessage(seenDto);
        Conversation conversation = getConversationById(seenDto.conversationId());
        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setStatus(MessageStatus.READ);
            conversation.setUnreadCount(conversation.getUnreadCount() - 1);
        }
        chatMessageRepository.saveAll(chatMessages);
        conversationRepository.save(conversation);
        for (ChatMessage chatMessage : chatMessages) {
            messagingTemplate.convertAndSendToUser(chatMessage.getSender().getUserId(),
                    "/queue/messages", new MessageStatusDto(chatMessage.getChatMessageId()
                            , chatMessage.getStatus().toString(), null,conversation.getConversationId()));
        }


    }

    @Override
    public ApiResponseDto getChatMessages(String conversationId) {
        return new ApiResponseDto(true,"Messages fetched successfully"
                ,chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId));
    }


    private ChatMessage getChatMessage(String messageId) {
        return chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "id", messageId));
    }

    private List<ChatMessage> getChatMessagesAfterLastSeenMessage(SeenDto seenDto) {
        return chatMessageRepository
                .findByConversationIdAndSenderIdNotAndStatusNot(seenDto.conversationId(),seenDto.userId());
    }

    private Conversation getConversationById(String id){
        return conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", id));
    }

}
