package com.dimu.dimuapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    @JsonIgnore
    private Conversation conversation;

    @ManyToOne
    @JoinColumn( name = "userId")
    private User user;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int unreadCount = 0;
}
