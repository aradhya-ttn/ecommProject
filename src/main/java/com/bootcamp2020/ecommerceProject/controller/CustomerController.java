package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.CustomerDao;
import com.bootcamp2020.ecommerceProject.dto.CustomerProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @GetMapping(value = "/profile")
    public CustomerProfileDto getProfile(HttpServletRequest request){
       return customerDao.getMyProfile(request);
    }
}
