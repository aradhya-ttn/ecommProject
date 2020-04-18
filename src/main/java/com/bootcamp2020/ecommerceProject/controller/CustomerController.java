package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.CategoryDao;
import com.bootcamp2020.ecommerceProject.dao.CustomerDao;
import com.bootcamp2020.ecommerceProject.dto.CustomerAddressDto;
import com.bootcamp2020.ecommerceProject.dto.CustomerCategoryDto;
import com.bootcamp2020.ecommerceProject.dto.CustomerProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CategoryDao categoryDao;

    @GetMapping(value = "/profile")
    public CustomerProfileDto getProfile(HttpServletRequest request){
       return customerDao.getMyProfile(request);
    }

    @GetMapping(value = "/address")
    public List<CustomerAddressDto> getAddress(HttpServletRequest request){
        return customerDao.getAddress(request);
    }

    @PatchMapping(value = "/updateProfile")
    public ResponseEntity updateProfile(@RequestBody HashMap<String ,Object> map, HttpServletRequest request, WebRequest webRequest){
        return customerDao.updateProfile(map,request,webRequest);
    }

    @PutMapping(value = "/updatePassword")
    private String updatePassword( @RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword,HttpServletRequest request,WebRequest webRequest) {
        return customerDao.updatePassword(webRequest, request, password, confirmPassword);
    }

    @PostMapping(value = "/addAddress")
    public String addNewAddress(@RequestBody CustomerAddressDto customerAddressDto,HttpServletRequest request,WebRequest webRequest){
        return customerDao.addAddress(request, webRequest, customerAddressDto);
    }

    @DeleteMapping(value = "/deleteAddress")
    public String deleteAddress(@RequestParam Long id,HttpServletRequest request,WebRequest webRequest){
        return customerDao.deleteAddress(id, request, webRequest);
    }

    @PutMapping(value = "/updateAddress")
    public String updateAddress(@RequestParam("addressId")Long id,@RequestBody HashMap<String,Object> value,HttpServletRequest request,WebRequest webRequest){
        return customerDao.updateAddress(value,id,request,webRequest);
    }

    @GetMapping(value = "/getAllCategory")
    public List<CustomerCategoryDto> getAllCategoryForCustomer(@RequestParam("categoryId") Long categoryId){
        return categoryDao.getAllCategoryForCustomer(categoryId);
    }
}