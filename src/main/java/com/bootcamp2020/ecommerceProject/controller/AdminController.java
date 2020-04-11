package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.AdminDao;
import com.bootcamp2020.ecommerceProject.dto.AllCustomerDto;
import com.bootcamp2020.ecommerceProject.dto.AllSellerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
   private AdminDao adminDao;

    @GetMapping("/customers")
    private List<AllCustomerDto> getAllCustomer(
            @RequestParam(defaultValue = "0") String pageOffset,
            @RequestParam(defaultValue = "10") String pageSize,
            @RequestParam(defaultValue = "id") String field
    ){
        return adminDao.returnCustomer(pageOffset,pageSize,field);
    }

    @GetMapping("/sellers")
    private List<AllSellerDto> getAllSeller(
            @RequestParam(defaultValue = "0") String pageOffset,
            @RequestParam(defaultValue = "10") String pageSize,
            @RequestParam(defaultValue = "id") String field
    ){
        List<AllSellerDto> allSellerDtos = adminDao.returnSeller(pageOffset, pageSize, field);
        return allSellerDtos;
    }
    @PutMapping("/activateCustomer")
    private String activateCustomer(WebRequest webRequest,@RequestParam("id") Long id){
        return adminDao.activateCustomer(id,webRequest);
    }

    @PutMapping("/deactivateCustomer")
    private String deActivateCustomer(WebRequest webRequest, @RequestParam("id") Long id){
        return adminDao.deActivateCustomer(id, webRequest);
    }
    @PutMapping("/activateSeller")
    private String activateSeller(WebRequest webRequest,@RequestParam("id") Long id){
        return adminDao.activateSeller(id,webRequest);
    }

    @PutMapping("/deactivateSeller")
    private String deActivateSeller(WebRequest webRequest, @RequestParam("id") Long id){
        return adminDao.deActivateSeller(id, webRequest);
    }

}
