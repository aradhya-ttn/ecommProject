package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.CustomerDao;
import com.bootcamp2020.ecommerceProject.dao.SellerDao;
import com.bootcamp2020.ecommerceProject.dto.CustomerRegisterDto;
import com.bootcamp2020.ecommerceProject.dto.SellerRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    public CustomerDao customerDao;

    @Autowired
    public SellerDao sellerDao;

    @Autowired
    MessageSource messageSource;

    @PostMapping("/customer")
    public  String doRegister(@Valid @RequestBody CustomerRegisterDto customerRegister, WebRequest webRequest){
        customerDao.doRegisterCustomer(customerRegister,webRequest);
        Locale locale= webRequest.getLocale();
        String message= messageSource.getMessage("auth.message.successfull",null,locale);
        return message;
    }
    @PostMapping("/seller")
    public  String doRegister(@Valid @RequestBody SellerRegisterDto sellerRegisterDto, WebRequest webRequest) {
        sellerDao.doRegisterSeller(sellerRegisterDto,webRequest);
        Locale locale= webRequest.getLocale();
        String message= messageSource.getMessage("auth.message.successfull",null,locale);
        return message;
    }
}
