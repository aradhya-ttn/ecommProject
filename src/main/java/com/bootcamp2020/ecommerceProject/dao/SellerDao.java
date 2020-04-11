package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.SellerRegisterDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.GstNumberAlreadyExist;
import com.bootcamp2020.ecommerceProject.repositories.SellerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Component
public class SellerDao {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    MessageSource messageSource;
    @Autowired
    public JavaMailSender javaMailSender;


    public void doRegisterSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto, WebRequest webRequest) {

        Locale locale=webRequest.getLocale();

        if (userRepository.findByEmail(sellerRegisterDto.getEmail()) != null) {
            String message=messageSource.getMessage("exp.message.alreadyExist",null,locale);
            throw new EmailException(message);
        } else {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setEmail(sellerRegisterDto.getEmail());
            Name name = new Name();
            name.setFirstName(sellerRegisterDto.getFirstName());
            name.setMiddleName(sellerRegisterDto.getMiddleName());
            name.setLastName(sellerRegisterDto.getLastName());
            user.setName(name);
            user.setActive(false);
            user.setPassword(passwordEncoder.encode(sellerRegisterDto.getPassword()));
            user.setRoles(Arrays.asList(new Role("ROLE_SELLER")));
            Seller seller = new Seller();
            seller.setUser(user);
            seller.setCompanyContact(sellerRegisterDto.getCompanyContactNo());
            seller.setCompanyName(sellerRegisterDto.getCompanyName());
            if (sellerRepository.findByGst(sellerRegisterDto.getGst()) != null) {
                String message = messageSource.getMessage("gst.message.alreadyExist", null, locale);
                throw new GstNumberAlreadyExist(message);
            } else {
                seller.setGst(sellerRegisterDto.getGst());
            }
            //        userRepository.save(user);
            sellerRepository.save(seller);

//            Generating token for activation of user


            String token = UUID.randomUUID().toString();

            VerificationToken verificationToken= new VerificationToken();
            verificationTokenRepository.save(new VerificationToken(token,user,
                    verificationToken.calculateExpiryDate(verificationToken.getEXPIRATION())));
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            String recipientAddress = user.getEmail();
            String confirmationUrl
                    = webRequest.getContextPath()+ "/registrationConfirm?token=" + token;
            String message = messageSource.getMessage("auth.message.register.confirm", null, locale);
            String subject = message;
            mailMessage.setTo(recipientAddress);
            mailMessage.setSubject(subject);
            mailMessage.setText(subject + "\r\n" + "http://localhost:8080" + confirmationUrl);
            javaMailSender.send(mailMessage);
        }
    }
}
