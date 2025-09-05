package com.dimu.dimuapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.boot.Metadata;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SocketMessage {
    private String to;
    private String from;
    private String content;
    private String subject;
    private String link;
    private LocalDateTime date = LocalDateTime.now();

    private Metadata data;

    public SocketMessage(SocketMessage message){
        this.to = message.getTo();
        this.from = message.getFrom();
        this.subject = message.getSubject();
        this.link = message.getLink();
        this.date = message.getDate();
        this.data = message.getData();
    }

    @Data
    public static class Metadata{
        private String agreementId;
        private String transactionId;
        private String goodServiceId;
    }


}
