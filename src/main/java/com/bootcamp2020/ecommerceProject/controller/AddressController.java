package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.AddressDao;
import com.bootcamp2020.ecommerceProject.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    @Autowired
    AddressDao addressDao;

    @GetMapping("/address/user/{id}")
    public Address getAddress(@PathVariable Long id){

       return addressDao.getAddress(id);
    }

}
