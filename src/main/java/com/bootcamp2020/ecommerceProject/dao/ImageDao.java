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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


@Component
public class ImageDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    public String uploadImageForUser(MultipartFile imageFile , WebRequest webRequest, HttpServletRequest request) throws IOException {
        Locale locale=webRequest.getLocale();
        if (imageFile.isEmpty()) {
            String emptyMessage=messageSource.getMessage("msg.image.empty",null,locale);
            return emptyMessage;
        }
        try {
            boolean isImageValid=imageValidator(imageFile);
             if(isImageValid){
            // Get the file and save it somewhere
            String folder="/home/aradhya/ecommProject/users/";
            byte[] bytes = imageFile.getBytes();
            String extension= getFileExtension(imageFile);
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
    private String getFileExtension(MultipartFile imageFile){
        String fileName=imageFile.getOriginalFilename();
        int i = fileName.lastIndexOf(".");
        String extension=fileName.substring(i);
        return extension;
    }

    private Boolean imageValidator(MultipartFile image){
//        String regex= "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(image.getName());
//        Boolean isImageValid=matcher.matches();
        String fileExtension = getFileExtension(image);
        if(fileExtension.equalsIgnoreCase(".jpg") ||(fileExtension.equalsIgnoreCase(".jpeg")) ||(fileExtension.equalsIgnoreCase(".png"))||(fileExtension.equalsIgnoreCase(".bmp"))){
            return true;
        }else{
            return  false;
        }
    }

    public String uploadImageForProduct(MultipartFile imageFile , WebRequest webRequest, Long productId) throws IOException {
        Locale locale=webRequest.getLocale();
        if (imageFile.isEmpty()) {
            String emptyMessage=messageSource.getMessage("msg.image.empty",null,locale);
            return emptyMessage;
        }
        try {
            boolean isImageValid=imageValidator(imageFile);
            if(isImageValid){
                // Get the file and save it somewhere
                String folder="/home/aradhya/ecommProject/products/product_id/";
                byte[] bytes = imageFile.getBytes();
                String extension= getFileExtension(imageFile);
                String imageName=productId+extension;
                Path path=Paths.get(folder+imageName);
                Files.write(path, bytes);
                return folder+imageName;
            }else{
                String message=messageSource.getMessage("msg.image.invalid",null,locale);
                throw new EmailException(message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public String uploadPrimaryImage(MultipartFile imageFile , WebRequest webRequest, Long productId) throws IOException {
        Locale locale=webRequest.getLocale();
        if (imageFile.isEmpty()) {
            String emptyMessage=messageSource.getMessage("msg.image.empty",null,locale);
            return emptyMessage;
        }
        try {
            boolean isImageValid=imageValidator(imageFile);
            if(isImageValid){
                // Get the file and save it somewhere
                String folder="/home/aradhya/ecommProject/products/product_id/variations/";
                byte[] bytes = imageFile.getBytes();
                String extension= getFileExtension(imageFile);
                String imageName=productId+extension;
                Path path=Paths.get(folder+imageName);
                Files.write(path, bytes);
                return folder+imageName;
            }else{
                String message=messageSource.getMessage("msg.image.invalid",null,locale);
                throw new EmailException(message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public Set<String> uploadSecondaryImage(List<MultipartFile> images , WebRequest webRequest, Long productId) throws IOException {
        Locale locale = webRequest.getLocale();
        Set<String> paths=new HashSet<>();
        int count=0;
        for (MultipartFile image:images) {
            try {
                ++count;
            boolean isImageValid=imageValidator(image);
            if(isImageValid){
                // Get the file and save it somewhere
                String folder="/home/aradhya/ecommProject/products/product_id/variations/";
                byte[] bytes = image.getBytes();
                String extension= getFileExtension(image);
                String imageName=productId+"."+count+extension;
                Path path=Paths.get(folder+imageName);
                Files.write(path, bytes);
                paths.add(folder+imageName);
            }else{
                String message=messageSource.getMessage("msg.image.invalid",null,locale);
                throw new EmailException(message);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return paths;
    }

}

