package com.bootcamp2020.ecommerceProject.controller;


import com.bootcamp2020.ecommerceProject.dao.ForgotPasswordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/password")
public class PasswordController {

    @Autowired
    private ForgotPasswordDao forgotPasswordDao;

    @PostMapping("/reset")
    private String resetPassword(@RequestParam("email")  String email, WebRequest webRequest){
        return forgotPasswordDao.sendForgotToken(email, webRequest);
    }
    @PutMapping("/update")
    private String updatePassword(@RequestParam("token")  String token, @RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword,WebRequest webRequest){
        return forgotPasswordDao.updatePassword(token, webRequest, password, confirmPassword);
    }

}
