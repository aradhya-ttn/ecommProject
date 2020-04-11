package com.bootcamp2020.ecommerceProject.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderDao {

    @Autowired
    public JavaMailSender javaMailSender;

    public String sendEamil() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("aradhyapatel@gmail.com");
        message.setSubject("forgot password");
        message.setText("your password is sadfghjk");

        javaMailSender.send(message);
        return "email send successfully";
    }


}
