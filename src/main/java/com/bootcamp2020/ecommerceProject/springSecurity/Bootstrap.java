package com.bootcamp2020.ecommerceProject.springSecurity;


import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.enums.Label;
import com.bootcamp2020.ecommerceProject.repositories.CustomerRepository;
import com.bootcamp2020.ecommerceProject.repositories.SellerRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.count()<1){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user1 = new User();
            user1.setEmail("aradhyapatel@gmail.com");
            user1.setActive(true);
            Name name= new Name();
            name.setFirstName("aradhya");
            name.setMiddleName("kumar");
            name.setLastName("patel");
            user1.setName(name);
            List<Address> addresses = new ArrayList<>();
            Address address= new Address();
            address.setAddress("14 cross Street");
            address.setCity("noida");
            address.setState("up");
            address.setCountry("india");
            address.setLabel(Label.home);
            address.setZipCode(201308);
            addresses.add(address);

            Customer customer= new Customer();
            customer.setAddresses(addresses);
            address.setCustomer(customer);
            customer.setUser(user1);
            customer.setContact("7754076598");
            customerRepository.save(customer);
            user1.setPassword(passwordEncoder.encode("pass"));
            user1.setRoles(Arrays.asList(new Role("ROLE_CUSTOMER")));

            User user2 = new User();
            user2.setEmail("aradhya2134@gmail.com");
            user2.setActive(true);
            Name name1= new Name();
            name1.setFirstName("aditya");
            name1.setMiddleName("kumar");
            name1.setLastName("patil");
            user2.setName(name1);
            Address address1= new Address();
            address1.setAddress("14 cross Street");
            address1.setCity("noida");
            address1.setState("up");
            address1.setCountry("india");
            address1.setLabel(Label.home);
            address1.setZipCode(201308);
            Seller seller=new Seller();
            seller.setGst("27AAPFU0939F1ZV");
            seller.setAddress(address1);
            address1.setSeller(seller);
            seller.setUser(user2);
            seller.setCompanyName("to the new");
            seller.setCompanyContact("7754076598");
            sellerRepository.save(seller);
            user2.setPassword(passwordEncoder.encode("pass"));
            user2.setRoles(Arrays.asList(new Role("ROLE_ADMIN"),new Role("ROLE_SELLER")));

            userRepository.save(user1);
            userRepository.save(user2);
            System.out.println("Total users saved::"+userRepository.count());

        }
    }
}
