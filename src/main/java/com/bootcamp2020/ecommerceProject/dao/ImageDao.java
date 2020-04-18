package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ImageDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    public String uploadImage(MultipartFile imageFile , WebRequest webRequest, HttpServletRequest request) throws IOException {
        Locale locale=webRequest.getLocale();
        if (imageFile.isEmpty()) {
            String emptyMessage=messageSource.getMessage("msg.image.empty",null,locale);
            return emptyMessage;
        }
        try {
            String regex= "([^\\s]+(\\.(?i)(jpg|png|gif|jpeg|bmp))$)";
             Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(imageFile.getName());
             Boolean isImageValid=matcher.matches();
             if(isImageValid){
            // Get the file and save it somewhere
            String folder="/home/aradhya/images/";
            byte[] bytes = imageFile.getBytes();
            String extension=getFileName(imageFile);
            Principal principal=request.getUserPrincipal();
            User user = userRepository.findByEmail(principal.getName());
            Long id=user.getId();
            String imageName=id+extension;
            Path path=Paths.get(folder+imageName);
            user.setImagePath(""+path);
            userRepository.save(user);
            Files.write(path, bytes);
             }else{
                 String message=messageSource.getMessage("msg.image.invalid",null,locale);
                 throw new EmailException(message);
             }
             String successMessage=messageSource.getMessage("msg.image.success",null,locale);
            return successMessage;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    private String getFileName(MultipartFile imageFile){
        String fileName=imageFile.getOriginalFilename();
        int i = fileName.lastIndexOf(".");
        String extension=fileName.substring(i);
        return extension;
    }

}

