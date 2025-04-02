package com.dimu.dimuapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String[] to;
    private String from;
    private String subject;
    private String link;

    public Mail(String[] to,String from, String subject){
        this.to = to;
        this.from = from;
        this.subject = subject;
    }
}
