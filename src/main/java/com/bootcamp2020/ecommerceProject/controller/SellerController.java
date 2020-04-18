package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.CategoryDao;
import com.bootcamp2020.ecommerceProject.dao.ProductDao;
import com.bootcamp2020.ecommerceProject.dao.SellerDao;
import com.bootcamp2020.ecommerceProject.dto.ProductDto;
import com.bootcamp2020.ecommerceProject.dto.SellerProfileDto;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.CategoryAndSubCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/seller")
public class SellerController {

    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

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

    @GetMapping("/viewAllCategory")
    private List<CategoryAndSubCategoryDto> viewAllCategoryForSeller(){
        return categoryDao.getAllCategoryForSeller();
    }

    @PostMapping("/addProduct")
    private String addProduct(@RequestBody ProductDto productDto,HttpServletRequest request,WebRequest webRequest){
        return productDao.addProduct(productDto, request, webRequest);
    }
}
