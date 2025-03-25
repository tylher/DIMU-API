package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.MessageStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String subject;
    private String content;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.UNREAD;

}
