package com.dimu.dimuapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SocketMessage {
    private String to;
    private String from;
    private String content;
    private String Subject;
    private String link;
    private LocalDateTime date = LocalDateTime.now();
}
