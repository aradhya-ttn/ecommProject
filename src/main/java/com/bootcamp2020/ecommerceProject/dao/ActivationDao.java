package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.entities.VerificationToken;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.InvalidTokenException;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Component
public class ActivationDao {
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MessageSource messageSource;

    public String confirmRegistration
            (String token,WebRequest request) {


        Locale locale = request.getLocale();

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null)  {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
//            model.addAttribute("message", message);
            throw new InvalidTokenException(message);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
//            model.addAttribute("message", messageValue);
//            return "Token has Expired ,please generate new token for registration" + locale.getLanguage();
            throw new InvalidTokenException(messageValue+"   "+verificationToken.getExpiryDate().getTime()+"   "+cal.getTime().getTime());
        }

        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.dodeleteActivateToken(token);
        String messageValue = messageSource.getMessage("auth.message.act.successfull", null, locale);
        return messageValue ;
    }

    public String sendReactivateEmail(String email, WebRequest webRequest){
        User user = userRepository.findByEmail(email);
        Locale locale = webRequest.getLocale();
        if (userRepository.findByEmail(email) == null) {
            String message = messageSource.getMessage("exp.message.notExist", null, locale);
            throw new EmailException(message);
        } else if (user.getActive()==true){
            String message=messageSource.getMessage("msg.aleady.activated",null,locale);
            return message;

        }else{
            Long id = user.getId();
            String token = UUID.randomUUID().toString();
            Date date = new VerificationToken().calculateExpiryDate(new VerificationToken().getEXPIRATION());
            verificationTokenRepository.doUpdateInfo(token, date, id);

            String recipientAddress = email;
            String messageValue = messageSource.getMessage("auth.message.reconfirm", null, locale);
            String subject = messageValue;
            String confirmationUrl
                    = webRequest.getContextPath() + "/registrationConfirm?token=" + token;
//        String message = messages.getMessage("message.regSucc", null, event.getLocale());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipientAddress);
            mailMessage.setSubject(subject);
            mailMessage.setText(mailMessage + "\r\n" + "http://localhost:8080" + confirmationUrl);
            mailSender.send(mailMessage);
            String message = messageSource.getMessage("auth.message.reconfirm.link", null, locale);
            return message;
        }
    }
}
