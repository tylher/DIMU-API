package com.dimu.dimuapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String conversationId;

    @ManyToMany
    @JoinTable(name = "conversation_participants"
            ,joinColumns = @JoinColumn(name = "conversationId")
            ,inverseJoinColumns = @JoinColumn(name = "userId",referencedColumnName = "userId")
    )
    private List<User> participants;

    private Integer unreadCount;

    private String lastMessage;

    private Instant lastMessageTime;



}
