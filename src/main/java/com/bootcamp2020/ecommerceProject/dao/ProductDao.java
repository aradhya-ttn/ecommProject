package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.ProductDto;
import com.bootcamp2020.ecommerceProject.entities.Category;
import com.bootcamp2020.ecommerceProject.entities.Product;
import com.bootcamp2020.ecommerceProject.entities.Seller;
import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.repositories.CategoryRepository;
import com.bootcamp2020.ecommerceProject.repositories.ProductRepository;
import com.bootcamp2020.ecommerceProject.repositories.SellerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class ProductDao {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public String addProduct(ProductDto productDto, HttpServletRequest request, WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String email=request.getUserPrincipal().getName();
        Long categoryId=productDto.getCategoryId();
        String brand = productDto.getBrand();
        String name = productDto.getName();
        Long userId = userRepository.findByEmail(email).getId();
        Seller seller = sellerRepository.findByUserId(userId);
        List<Category> categories = categoryRepository.findByParentId(categoryId);
        Category category = categoryRepository.findByid(categoryId);
        if(categories.isEmpty() && category!=null){
            Product prooduct = productRepository.getProoductByName(brand, categoryId, name, userId);
            if(prooduct==null){
                Product product=new Product();
                product.setName(productDto.getName());
                product.setBrand(productDto.getBrand());
                if(productDto.getCancellable()!=null) {
                    product.setCancellable(productDto.getCancellable());
                }else{
                    product.setCancellable(false);
                }
                if(productDto.getReturnable()!=null){
                    product.setReturnable(productDto.getReturnable());
                }else{
                    product.setReturnable(false);
                }
                if(productDto.getDescription()!=null){
                    product.setDescription(productDto.getDescription());
                }
                product.setCategory(category);
                product.setSeller(seller);
                productRepository.save(product);
                sendEmailToAdmin(product,webRequest);
            }else{
                String message=messageSource.getMessage("msg.productName.exist",null,locale);
                throw new EmailException(message);
            }
        }else{
            String message=messageSource.getMessage("msg.category.notValid",null,locale);
            throw new EmailException(message);
        }
        String message=messageSource.getMessage("msg.product.saved",null,locale);
        return message;
    }
    private void sendEmailToAdmin(Product product,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        List<User> allAdmin = userRepository.getAllAdmin();
        String messageBody="Product : " +"\n" +
                " seller=" + product.getSeller().getCompanyName()+"\n" +
                " category=" + product.getCategory().getName() +"\n" +
                " name=" + product.getName() +"\n" +
                " description=" + product.getDescription() +"\n" +
                " isCancellable=" + product.getCancellable() +"\n" +
                " isReturnable=" + product.getReturnable() +"\n" +
                " brand=" + product.getBrand() + "\n"
                ;
        String confirmationUrl
                = webRequest.getContextPath() + "/admin/activateProduct?productId=" + product.getId();
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        String actMessage=messageSource.getMessage("msg.newProduct.toActivate",null,locale);
        mailMessage.setText(messageBody +"\n"+actMessage+ "\r\n" + "http://localhost:8080" + confirmationUrl);
        String message=messageSource.getMessage("msg.newProduct.received",null,locale);
        mailMessage.setSubject(message);
        mailMessage.setSentDate(new Date());
        for (User user:allAdmin) {
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom(user.getEmail());
            javaMailSender.send(mailMessage);
        }
    }
}
