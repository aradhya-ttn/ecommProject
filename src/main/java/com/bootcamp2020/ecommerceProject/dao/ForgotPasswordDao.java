package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.entities.ForgotPasswordToken;
import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.InvalidTokenException;
import com.bootcamp2020.ecommerceProject.exceptions.PasswordException;
import com.bootcamp2020.ecommerceProject.repositories.ForgotPasswordTokenRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ForgotPasswordDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String sendForgotToken(String email, WebRequest webRequest) {
        Locale locale = webRequest.getLocale();
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        Boolean isEmailValid = matcher.matches();

        if (isEmailValid) {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                String emailDoestNotExist = messageSource.getMessage("exp.message.notExist", null, locale);
                throw new EmailException(emailDoestNotExist);
            } else {
                if (user.getActive() == false) {
                    String deactivatedAccount = messageSource.getMessage("exp.message.deactivated", null, locale);
                    throw new EmailException(deactivatedAccount);
                } else {

                    Long id=user.getId();
                    forgotPasswordTokenRepository.deleteTokenById(id);
                    String token = UUID.randomUUID().toString();
                    ForgotPasswordToken passwordToken = new ForgotPasswordToken();
                    forgotPasswordTokenRepository.save(new ForgotPasswordToken(token, user,
                            passwordToken.calculateExpiryDate(passwordToken.getEXPIRATION())));
                    SimpleMailMessage mailMessage = new SimpleMailMessage();

                    String recipientAddress = user.getEmail();
                    String confirmationUrl
                            = webRequest.getContextPath() + "/password/update?token=" + token;
                    String message = messageSource.getMessage("auth.message.resetPassword", null, locale);
                    String subject = message;
                    mailMessage.setTo(recipientAddress);
                    mailMessage.setSubject(subject);
                    mailMessage.setText(subject + "\r\n" + "http://localhost:8080" + confirmationUrl);
                    javaMailSender.send(mailMessage);
                    String resetLink = messageSource.getMessage("msg.reset.link", null, locale);
                    return resetLink;
                }
            }
        } else {
            String emailNotExist = messageSource.getMessage("exp.message.notExist", null, locale);
            throw new EmailException(emailNotExist);
        }
    }

    public String updatePassword(String token, WebRequest webRequest,@Valid  String password, String confirmPassword){
        Locale locale=webRequest.getLocale();
        String regex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,15})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        Boolean isPasswordValid = matcher.matches();

        if(password.equals(confirmPassword)==true){
            ForgotPasswordToken passwordToken=forgotPasswordTokenRepository.findByToken(token);
            User user=passwordToken.getUser();
            Calendar cal = Calendar.getInstance();

            if (passwordToken == null)  {
                String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
                throw new InvalidTokenException(message);
            }
            else  if ((passwordToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
                forgotPasswordTokenRepository.deleteToken(token);
                throw new InvalidTokenException(messageValue+"   "+passwordToken.getExpiryDate().getTime()+"   "+cal.getTime().getTime());
            }else if(isPasswordValid==false){
                String passwordNotValid=messageSource.getMessage("exp.message.notValid",null,locale);
                throw new PasswordException(passwordNotValid);

            } else{
                String encryptedPassword = passwordEncoder.encode(password);
                String email=user.getEmail();
                userRepository.updatePassword(encryptedPassword,email);
                forgotPasswordTokenRepository.deleteToken(token);
                String successMessage=messageSource.getMessage("msg.password.updated",null,locale);
                return successMessage;
            }

        }else{
            String passwordDoNotMatch=messageSource.getMessage("msg.password.notMatch",null,locale);
            throw new PasswordException(passwordDoNotMatch);
        }
    }
}





