package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.CustomerRegisterDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.repositories.CustomerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Component
public class CustomerDao {

    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    public void doRegisterCustomer(CustomerRegisterDto customerRegister, WebRequest webRequest) {
        Locale locale=webRequest.getLocale();

        if (userRepository.findByEmail(customerRegister.getEmail()) != null) {
            String message=messageSource.getMessage("exp.message.alreadyExist",null,locale);
            throw new EmailException(message);
        } else {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setEmail(customerRegister.getEmail());
            Name name = new Name();
            name.setFirstName(customerRegister.getFirstName());
            name.setMiddleName(customerRegister.getMiddleName());
            name.setLastName(customerRegister.getLastName());
            user.setName(name);
            user.setActive(false);
            user.setPassword(passwordEncoder.encode(customerRegister.getPassword()));
            Customer customer = new Customer();
            customer.setUser(user);
            customer.setContact(customerRegister.getContactNo());
            user.setRoles(Arrays.asList(new Role("ROLE_CUSTOMER")));
//            userRepository.save(user);
            customerRepository.save(customer);


//       Generating token for activation of user


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
