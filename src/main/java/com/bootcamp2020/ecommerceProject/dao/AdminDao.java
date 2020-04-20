package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.AllCustomerDto;
import com.bootcamp2020.ecommerceProject.dto.AllSellerDto;
import com.bootcamp2020.ecommerceProject.entities.Customer;
import com.bootcamp2020.ecommerceProject.entities.Product;
import com.bootcamp2020.ecommerceProject.entities.Seller;
import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.UserNotFoundException;
import com.bootcamp2020.ecommerceProject.repositories.CustomerRepository;
import com.bootcamp2020.ecommerceProject.repositories.ProductRepository;
import com.bootcamp2020.ecommerceProject.repositories.SellerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class AdminDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<AllCustomerDto> returnCustomer(String pageOffset, String pageSize, String field){
        Integer offSetPage=Integer.parseInt(pageOffset);
        Integer sizeOfPage= Integer.parseInt(pageSize);

        PageRequest pageRequest=PageRequest.of(offSetPage,sizeOfPage, Sort.Direction.ASC,field);

        List<AllCustomerDto>  allCustomerDtos= new ArrayList<>();
        List<Object[]> objects=userRepository.allCustomers(pageRequest);
        for (Object[] customer:objects) {
            allCustomerDtos.add(new AllCustomerDto((BigInteger)customer[0],((String)customer[1]+" "+(String)customer[2]
                    +" "+(String)customer[3]),(String)customer[4],(Boolean)customer[5],(String)customer[6]));
        }
        return allCustomerDtos;
    }
    public List<AllSellerDto> returnSeller(String pageOffset, String pageSize, String field){
        Integer offSetPage=Integer.parseInt(pageOffset);
        Integer sizeOfPage= Integer.parseInt(pageSize);

        PageRequest pageRequest=PageRequest.of(offSetPage,sizeOfPage, Sort.Direction.ASC,field);

        List<AllSellerDto>  allSellerDtos= new ArrayList<>();
        List<Object[]> objects=userRepository.allSellers(pageRequest);

        for (Object[] customer:objects) {
            allSellerDtos.add(new AllSellerDto((BigInteger)customer[0],(String)customer[1]+" "+(String)customer[2]
                    +" "+(String)customer[3],(String)customer[4],(Boolean)customer[5],(String)customer[6],(String)customer[7],(String)customer[8]));
        }

        return allSellerDtos;
    }


    public String activateCustomer(Long id, WebRequest webRequest){

        Locale locale=webRequest.getLocale();
        Customer customer=customerRepository.findByUserId(id);
        if(customer!=null ){
            User userDetail=userRepository.findById(id);
            if(userDetail.getActive()==true){
            String message= messageSource.getMessage("auth.message.activated",null,locale);
            return message;
            }else{
                userDetail.setActive(true);
                String message= messageSource.getMessage("auth.message.activated",null,locale);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(userDetail.getEmail());
                mailMessage.setSubject(message);
                mailMessage.setText("Hi "+userDetail.getName().getFirstName()+" your "+message);
                javaMailSender.send(mailMessage);
                userRepository.save(userDetail);
                return message;
            }

        }else {
            String message=messageSource.getMessage("exp.message.notFound",null,locale);
            throw new UserNotFoundException(message);
        }


    }
    public String deActivateCustomer(Long id, WebRequest webRequest){

        Locale locale=webRequest.getLocale();
        Customer customer=customerRepository.findByUserId(id);
        if(customer!=null ){
            User userDetail=userRepository.findById(id);
            if(userDetail.getActive()==false){
                String message= messageSource.getMessage("auth.message.deActivated",null,locale);
                return message;
            }else{
                userDetail.setActive(false);
                String message= messageSource.getMessage("auth.message.deActivated",null,locale);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(userDetail.getEmail());
                mailMessage.setSubject(message);
                mailMessage.setText("Hi "+userDetail.getName().getFirstName()+" your "+message);
                javaMailSender.send(mailMessage);
                userRepository.save(userDetail);
                return message;
            }

        }else {
            String message=messageSource.getMessage("exp.message.notFound",null,locale);
            throw new UserNotFoundException(message);
        }

    }

    public String activateSeller(Long id, WebRequest webRequest){

        Locale locale=webRequest.getLocale();
        Seller seller=sellerRepository.findByUserId(id);
        if(seller!=null ){
            User userDetail=userRepository.findById(id);
            if(userDetail.getActive()==true){
                String message= messageSource.getMessage("auth.message.activated",null,locale);
                return message;
            }else{
                userDetail.setActive(true);
                String message= messageSource.getMessage("auth.message.activated",null,locale);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(userDetail.getEmail());
                mailMessage.setSubject(message);
                mailMessage.setText("Hi "+userDetail.getName().getFirstName()+" your "+message);
                javaMailSender.send(mailMessage);
                userRepository.save(userDetail);
                return message;
            }

        }else {
            String message=messageSource.getMessage("exp.message.notFound",null,locale);
            throw new UserNotFoundException(message);
        }


    }
    public String deActivateSeller(Long id, WebRequest webRequest){

        Locale locale=webRequest.getLocale();
        Seller seller =sellerRepository.findByUserId(id);
        if(seller!=null ){
            User userDetail=userRepository.findById(id);
            if(userDetail.getActive()==false){
                String message= messageSource.getMessage("auth.message.deActivated",null,locale);
                return message;
            }else{
                userDetail.setActive(false);
                String message= messageSource.getMessage("auth.message.deActivated",null,locale);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(userDetail.getEmail());
                mailMessage.setSubject(message);
                mailMessage.setText("Hi "+userDetail.getName().getFirstName()+" your "+message);
                javaMailSender.send(mailMessage);
                userRepository.save(userDetail);
                return message;
            }

        }else {
            String message=messageSource.getMessage("exp.message.notFound",null,locale);
            throw new UserNotFoundException(message);
        }

    }
    public String activateProduct(Long productId,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Product product = productRepository.findByid(productId);
        if(product==null){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        if(!product.getActive()) {
            product.setActive(true);
            productRepository.save(product);

            String email = product.getSeller().getUser().getEmail();
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(email);
            String message=messageSource.getMessage("msg.seller.activated",null,locale);
            mailMessage.setSubject(message);
            String textMessage=messageSource.getMessage("msg.product.activated",null,locale);
            String adminMessage=messageSource.getMessage("msg.by.admin",null,locale);
            mailMessage.setText(textMessage+" "+product.getName()+" "+adminMessage);
            String successMessage=messageSource.getMessage("msg.Activation.success",null,locale);
            javaMailSender.send(mailMessage);
            return successMessage;
        }else{
            String message=messageSource.getMessage("msg.product.alreadyActivated",null,locale);
            return  message;
        }
    }
    public String deActivateProduct(Long productId,WebRequest webRequest) {
        Locale locale = webRequest.getLocale();
        Product product = productRepository.findByid(productId);
        if (product == null) {
            String message = messageSource.getMessage("msg.invalid.productId", null, locale);
            throw new EmailException(message);
        }
        if (product.getActive()) {
            product.setActive(false);
            productRepository.save(product);
            String messageBody = "Product : " + "\n" +
                    " productId=" + product.getId() + "\n" +
                    " seller=" + product.getSeller().getCompanyName() + "\n" +
                    " category=" + product.getCategory().getName() + "\n" +
                    " name=" + product.getName() + "\n" +
                    " description=" + product.getDescription() + "\n" +
                    " isCancellable=" + product.getCancellable() + "\n" +
                    " isReturnable=" + product.getReturnable() + "\n" +
                    " brand=" + product.getBrand() + "\n";
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setText(messageBody);
            mailMessage.setTo(product.getSeller().getUser().getEmail());
            String deactivateMessage = messageSource.getMessage("msg.product.deActivated", null, locale);
            mailMessage.setSubject(deactivateMessage);
            javaMailSender.send(mailMessage);
            return deactivateMessage;
        } else {
            String message = messageSource.getMessage("msg.product.alreadyDeActivated", null, locale);
            return message;
        }
    }
}
