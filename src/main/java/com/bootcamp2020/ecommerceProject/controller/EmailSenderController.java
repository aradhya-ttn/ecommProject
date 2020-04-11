package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.EmailSenderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/email")
public class EmailSenderController {

    @Autowired
    EmailSenderDao emailSenderDao;

    @GetMapping("/sendEmail")
    public String sendEmail(){

       return emailSenderDao.sendEamil();
    }



}
