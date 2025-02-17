package com.dimu.dimuapi.service.email;

import com.dimu.dimuapi.model.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendSimpleMail(Mail mail, String content) throws Exception {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            message.setText(content);
            message.setFrom(mail.getFrom());
            mailSender.send(message);
        }catch(Exception e){
            throw new Exception("Unable to send mail: "+ e.getMessage());
        }

    }


    public void sendHTMLMail(Mail mail, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(mail.getSubject());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom());
        helper.setText(content,true);

        mailSender.send(message);
    }
}
