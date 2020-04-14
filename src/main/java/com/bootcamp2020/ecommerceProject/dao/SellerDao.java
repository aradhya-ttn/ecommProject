package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.SellerProfileDto;
import com.bootcamp2020.ecommerceProject.dto.SellerRegisterDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.enums.Label;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.GstNumberAlreadyExist;
import com.bootcamp2020.ecommerceProject.exceptions.PasswordException;
import com.bootcamp2020.ecommerceProject.exceptions.UserNotFoundException;
import com.bootcamp2020.ecommerceProject.repositories.AddressRepository;
import com.bootcamp2020.ecommerceProject.repositories.SellerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SellerDao {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;


    public void doRegisterSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto, WebRequest webRequest) {

        Locale locale=webRequest.getLocale();

        if (userRepository.findByEmail(sellerRegisterDto.getEmail()) != null) {
            String message=messageSource.getMessage("exp.message.alreadyExist",null,locale);
            throw new EmailException(message);
        } else {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setEmail(sellerRegisterDto.getEmail());
            Name name = new Name();
            name.setFirstName(sellerRegisterDto.getFirstName());
            name.setMiddleName(sellerRegisterDto.getMiddleName());
            name.setLastName(sellerRegisterDto.getLastName());
            user.setName(name);
            user.setActive(false);
            user.setPassword(passwordEncoder.encode(sellerRegisterDto.getPassword()));
            user.setRoles(Arrays.asList(new Role("ROLE_SELLER")));
            Seller seller = new Seller();
            seller.setUser(user);
            seller.setCompanyContact(sellerRegisterDto.getCompanyContactNo());
            seller.setCompanyName(sellerRegisterDto.getCompanyName());
            if (sellerRepository.findByGst(sellerRegisterDto.getGst()) != null) {
                String message = messageSource.getMessage("gst.message.alreadyExist", null, locale);
                throw new GstNumberAlreadyExist(message);
            } else {
                seller.setGst(sellerRegisterDto.getGst());
            }
            //        userRepository.save(user);
            sellerRepository.save(seller);

//            Generating token for activation of user


            String token = UUID.randomUUID().toString();

            VerificationToken verificationToken= new VerificationToken();
            verificationTokenRepository.save(new VerificationToken(token,user,
                    verificationToken.calculateExpiryDate(verificationToken.getEXPIRATION())));
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            String recipientAddress = user.getEmail();
            String confirmationUrl
                    = webRequest.getContextPath()+ "/registrationConfirm?token=" + token;
            String message = messageSource.getMessage("auth.message.register.confirm", null, locale);
            String subject = message;
            mailMessage.setTo(recipientAddress);
            mailMessage.setSubject(subject);
            mailMessage.setText(subject + "\r\n" + "http://localhost:8080" + confirmationUrl);
            javaMailSender.send(mailMessage);
        }
    }
    public SellerProfileDto getProfile(HttpServletRequest httpServletRequest){
        String email=httpServletRequest.getUserPrincipal().getName();
        Long id=userRepository.findByEmail(email).getId();
        SellerProfileDto sellerProfileDto=null;
        List<Object[]> sellers=sellerRepository.sellerProfile(id);
        for (Object[] seller:sellers) {
                sellerProfileDto=new SellerProfileDto((BigInteger)seller[0],(String)seller[1],(String)seller[2],(Boolean)seller[3],(String)seller[4],(String)seller[5],(String)seller[6],
                        (String)seller[7],(String)seller[8],(String)seller[9],(String)seller[10],(String)seller[11],(Integer)seller[12]);
        }
        return sellerProfileDto;
    }
    private Boolean checkGst(String gst){
        String regexp ="(\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1})";
        Pattern pattern=Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(gst);
        return matcher.matches();
    }
    private Boolean checkNullValue(String data){
        if(data!=null && !data.equals(""))
            return true;
        else
            return false;
    }
    public String updateProfile(HashMap<String,Object> sellerDetails,HttpServletRequest request,WebRequest webRequest) {
        Locale locale = webRequest.getLocale();
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Seller seller = sellerRepository.findByUserId(user.getId());
        Name name = user.getName();
        String firstName = (String) sellerDetails.get("firstName");
        String middleName = (String) sellerDetails.get("middleName");
        String lastName = (String) sellerDetails.get("lastName");
        String companyContact = (String) sellerDetails.get("companyContact");
        String companyName = (String) sellerDetails.get("companyName");
        String gst = (String) sellerDetails.get("gst");
        if (checkNullValue(gst)){
            if (checkGst(gst)!=true) {
                String message = messageSource.getMessage("msg.gst.notValid", null, locale);
                throw new GstNumberAlreadyExist(message);
            }else{
                seller.setGst(gst);
            }
        }
        if (checkNullValue(firstName)) {
            name.setFirstName(firstName);
        }
        if (checkNullValue(middleName)){
            name.setMiddleName(middleName);
        }
        if (checkNullValue(lastName)){
            name.setLastName(lastName);
        }
        if(checkNullValue(companyContact)){
            seller.setCompanyContact(companyContact);
        }
        if ((checkNullValue(companyName))){
            seller.setCompanyName(companyName);
        }

        user.setName(name);
        sellerRepository.save(seller);
        userRepository.save(user);
        String message=messageSource.getMessage("msg.profile.update",null,locale);
        return message;
    }
    public String updatePassword( WebRequest webRequest, HttpServletRequest request, String password, String confirmPassword) {
        Locale locale = webRequest.getLocale();
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        String regex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,15})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        Boolean isPasswordValid = matcher.matches();
        if (password.equals(confirmPassword)) {
            if (isPasswordValid) {
                String encryptedPassword = passwordEncoder.encode(password);
                userRepository.updatePassword(encryptedPassword, email);
                String message = messageSource.getMessage("msg.password.updated", null, locale);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                System.out.println(email);
                mailMessage.setTo(email);
                mailMessage.setSubject(message);
                mailMessage.setText("Hi " + user.getName().getFirstName() + message);
                javaMailSender.send(mailMessage);
                return message;

            } else {
                String message = messageSource.getMessage("exp.message.notValid", null, locale);
                throw new PasswordException(message);
            }
        }
        String message=messageSource.getMessage("msg.password.notMatch",null,locale);
        throw new PasswordException(message);
    }
    public String updateAddress(HashMap<String,Object> addressDetail,Long addressId,HttpServletRequest request,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        Long id=userRepository.findByEmail(email).getId();
        Address address=addressRepository.getAddressByadddressIdAndSellerId(id,addressId);
        if(address!=null){
            String address1= (String) addressDetail.get("address");
            String city= (String) addressDetail.get("city");
            String state= (String) addressDetail.get("state");
            String country= (String) addressDetail.get("country");
            Label label=null;
            String stringLabel= (String) addressDetail.get("label");
            Integer zipCode= (Integer) addressDetail.get("zipCode");
            if(checkNullValue(address1)){
                address.setAddress(address1);
            }
            if(checkNullValue(city)){
                address.setCity(city);
            }
            if (checkNullValue(state)){
                address.setState(state);
            }
            if(checkNullValue(country)){
                address.setCountry(country);
            }
            if(checkNullValue(stringLabel)){
                label=Label.valueOf(stringLabel);
                address.setLabel(label);
            }

            if(zipCode!=null){
                address.setZipCode(zipCode);
            }
            addressRepository.save(address);
            System.out.println("Address saved");
            String message=messageSource.getMessage("msg.address.update",null,locale);
            return message;
        }else{
            String exceptionMessage=messageSource.getMessage("msg.address.notFound",null,locale);
            throw  new UserNotFoundException(exceptionMessage);
        }
    }
}
