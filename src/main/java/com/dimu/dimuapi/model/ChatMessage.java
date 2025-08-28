package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String chatMessageId;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;
    private MessageStatus status = MessageStatus.SENT;
    private String content;
    private Instant createdAt;
}
