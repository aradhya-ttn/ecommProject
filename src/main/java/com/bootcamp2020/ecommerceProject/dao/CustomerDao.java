package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.CustomerAddressDto;
import com.bootcamp2020.ecommerceProject.dto.CustomerProfileDto;
import com.bootcamp2020.ecommerceProject.dto.CustomerRegisterDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.enums.Label;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.PasswordException;
import com.bootcamp2020.ecommerceProject.exceptions.UserNotFoundException;
import com.bootcamp2020.ecommerceProject.repositories.AddressRepository;
import com.bootcamp2020.ecommerceProject.repositories.CustomerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomerDao {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public void doRegisterCustomer(CustomerRegisterDto customerRegister, WebRequest webRequest) {
        Locale locale=webRequest.getLocale();

        if (userRepository.findByEmail(customerRegister.getEmail()) != null) {
            String message=messageSource.getMessage("exp.message.alreadyExist",null,locale);
            throw new EmailException(message);
        } else {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setEmail(customerRegister.getEmail());
            Name name = new Name();
            name.setFirstName(customerRegister.getFirstName());
            name.setMiddleName(customerRegister.getMiddleName());
            name.setLastName(customerRegister.getLastName());
            user.setName(name);
            user.setActive(false);
            user.setAccountNonLocked(true);
            user.setPassword(passwordEncoder.encode(customerRegister.getPassword()));
            Customer customer = new Customer();
            customer.setUser(user);
            customer.setContact(customerRegister.getContactNo());
            user.setRoles(Arrays.asList(new Role("ROLE_CUSTOMER")));
//            userRepository.save(user);
            customerRepository.save(customer);


//       Generating token for activation of user


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
    public CustomerProfileDto getMyProfile(HttpServletRequest request){
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        CustomerProfileDto customerProfileDto=null;
        List<Object[]> objects=userRepository.customerProfile(email);
        for (Object[] customer:objects) {
           customerProfileDto = new CustomerProfileDto((BigInteger) customer[0], (String)customer[1], (String)customer[2], (Boolean)customer[3],
                    (String)customer[4], (String)customer[5]);
        }
        return customerProfileDto;
    }

    public List<CustomerAddressDto> getAddress(HttpServletRequest request){
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        User user=userRepository.findByEmail(email);
        Long id=user.getId();
        List<CustomerAddressDto> addressDtos=new ArrayList<>();
        List<Address> objects=addressRepository.getCustomerAddress(id);
        objects.forEach(address -> {
             addressDtos.add(new CustomerAddressDto(address.getCity(),address.getState(),address.getCountry(),
                address.getAddress(),address.getLabel(),address.getZipCode()));

        });
        return addressDtos;
    }

    public ResponseEntity updateProfile(HashMap<String,Object> customerInfo ,HttpServletRequest request,WebRequest webRequest){
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        User user=userRepository.findByEmail(email);
        Locale locale = webRequest.getLocale();
        Customer customer=customerRepository.findByUserId(user.getId());
        String firstName= (String) customerInfo.get("firstName");
        String middleName= (String) customerInfo.get("middleName");
        String lastName= (String) customerInfo.get("lastName");
        String contact= (String) customerInfo.get("contact");
        String regex = "(^$|[0-9]{10})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contact);
        Boolean isContactValid = matcher.matches();
        Name name=user.getName();
        if(checkNullValue(firstName)){
            name.setFirstName(firstName);
        }
        if(checkNullValue(middleName)){
            name.setMiddleName(middleName);
        }
        if(checkNullValue(lastName)){
            name.setLastName(lastName);
        }
        if(isContactValid){
            if(checkNullValue(contact)){
                customer.setContact(contact);
            }
        }else{
            String exceptionMessage=messageSource.getMessage("exp.message.contact",null,locale);
            throw new EmailException(exceptionMessage);
        }
        user.setName(name);
        customerRepository.save(customer);
        userRepository.save(user);
        String successMessage=messageSource.getMessage("msg.profile.update",null,locale);
        return new ResponseEntity( successMessage, HttpStatus.OK);
    }
    private Boolean checkNullValue(String data){
        if(data!=null && !data.equals(""))
            return true;
        else
            return false;
    }


    public String updatePassword( WebRequest webRequest, HttpServletRequest request, String password, String confirmPassword) {
        Locale locale = webRequest.getLocale();
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        User user=userRepository.findByEmail(email);
        String regex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,15})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        Boolean isPasswordValid = matcher.matches();
        if(password.equals(confirmPassword)){
            if(isPasswordValid){
                String encryptedPassword = passwordEncoder.encode(password);
                userRepository.updatePassword(encryptedPassword,email);
                String message=messageSource.getMessage("msg.password.updated",null,locale);
                SimpleMailMessage mailMessage= new SimpleMailMessage();
                System.out.println(email);
                mailMessage.setTo(email);
                mailMessage.setSubject(message);
                mailMessage.setText("Hi "+user.getName().getFirstName()+message);
                javaMailSender.send(mailMessage);
                return message;

            }else{
                String message=messageSource.getMessage("exp.message.notValid",null,locale);
                throw new PasswordException(message);
            }
        }
        String message=messageSource.getMessage("msg.password.notMatch",null,locale);
        throw new PasswordException(message);
    }
    public String addAddress(HttpServletRequest request,WebRequest webRequest,CustomerAddressDto customerAddressDto){
        Locale locale=webRequest.getLocale();
        String email=request.getUserPrincipal().getName();
        Long id =userRepository.findByEmail(email).getId();
        Customer customer= customerRepository.findByUserId(id);
        Address address= new Address();
        address.setAddress(customerAddressDto.getAddress());
        address.setCity(customerAddressDto.getCity());
        address.setState(customerAddressDto.getState());
        address.setCountry(customerAddressDto.getCountry());
        address.setLabel(customerAddressDto.getLabel());
        address.setZipCode(customerAddressDto.getZipcode());
        address.setCustomer(customer);
        addressRepository.save(address);
        String message=messageSource.getMessage("msg.address.success",null,locale);
        return message;
    }
    public String deleteAddress(Long addressid,HttpServletRequest request,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String email=request.getUserPrincipal().getName();
        Long id=userRepository.findByEmail(email).getId();
        Address address=addressRepository.getAddressByadddressIdAndCustomerId(id,addressid);
        if(address!=null){
            addressRepository.deleteById(addressid);
            String message=messageSource.getMessage("msg.address.delete",null,locale);
            return message;
        }else{
            String message=messageSource.getMessage("msg.address.notFound",null,locale);
            throw new UserNotFoundException(message);
        }
    }
    public String updateAddress(HashMap<String,Object> addressDetail,Long addressId,HttpServletRequest request,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Principal principal=request.getUserPrincipal();
        String email=principal.getName();
        Long id=userRepository.findByEmail(email).getId();
        Address address=addressRepository.getAddressByadddressIdAndCustomerId(id,addressId);
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
