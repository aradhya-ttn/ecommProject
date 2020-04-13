package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private ImageDao imageDao;

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestBody MultipartFile imageFile , WebRequest webRequest, HttpServletRequest request)throws IOException {
                 return imageDao.uploadImage(imageFile,webRequest,request);
    }
}
