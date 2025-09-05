package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.Enum.MessageStatus;
import com.dimu.dimuapi.model.ChatMessage;
import com.dimu.dimuapi.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {
    @Query("SELECT c FROM ChatMessage c " +
            "WHERE c.conversation.conversationId = " +
            ":conversationId ORDER BY c.createdAt ASC")
    public List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(@Param("conversationId") String conversationId);

    @Query("SELECT c FROM ChatMessage c WHERE c.conversation.conversationId = :conversationId " +
            "AND c.sender.userId <> :userId AND c.status <> READ")
    public List<ChatMessage> findByConversationIdAndSenderIdNotAndStatusNot(@Param("conversationId") String conversationId,
                                                                            @Param("userId") String userId); //findByConversationIdAndSenderIdNotAndStatusNot(Conversation conversation)>
}
