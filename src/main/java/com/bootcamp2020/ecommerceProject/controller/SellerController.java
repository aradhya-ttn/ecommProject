package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.SellerDao;
import com.bootcamp2020.ecommerceProject.dto.SellerProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/seller")
public class SellerController {

    @Autowired
    private SellerDao sellerDao;

    @GetMapping(value = "/profile")
    public SellerProfileDto getProfile(HttpServletRequest request){
        return sellerDao.getProfile(request);
    }

    @PutMapping(value = "/updateProfile")
    public String updateProfile(@RequestBody HashMap<String,Object> value, HttpServletRequest request, WebRequest webRequest){
        return sellerDao.updateProfile(value, request, webRequest);
    }
    @PutMapping(value = "/updatePassword")
    private String updatePassword( @RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword,HttpServletRequest request,WebRequest webRequest) {
        return sellerDao.updatePassword(webRequest, request, password, confirmPassword);
    }
    @PutMapping(value = "/updateAddress")
    public String updateAddress(@RequestParam("addressId")Long id,@RequestBody HashMap<String,Object> value,HttpServletRequest request,WebRequest webRequest){
        return sellerDao.updateAddress(value,id,request,webRequest);
    }
}
