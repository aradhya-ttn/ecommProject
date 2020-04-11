package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.ActivationDao;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class RegistrationController {
   @Autowired
    private ActivationDao activationDao;

   @Autowired
     private UserRepository userRepository;

   @Autowired
    private MessageSource messageSource;

    @GetMapping("/registrationConfirm")
    private String confirmRegistration(WebRequest request, @RequestParam("token") String token){
        return activationDao.confirmRegistration(token,request);
    }

    @GetMapping("/registrationReconfirm")
    private String reconfirmActivation(WebRequest request, @RequestParam("email") String email) {
        return activationDao.sendReactivateEmail(email, request);
    }

}
