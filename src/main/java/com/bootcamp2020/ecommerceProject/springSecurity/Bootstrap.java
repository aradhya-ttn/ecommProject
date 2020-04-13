package com.bootcamp2020.ecommerceProject.springSecurity;


import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.enums.Label;
import com.bootcamp2020.ecommerceProject.repositories.CustomerRepository;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.count()<1){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user1 = new User();
            user1.setEmail("user");
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
            customer.setUser(user1);
            customer.setContact("7754076598");
            customerRepository.save(customer);
            user1.setPassword(passwordEncoder.encode("pass"));
            user1.setRoles(Arrays.asList(new Role("ROLE_CUSTOMER"),new Role("ROLE_SELLER")));

            User user2 = new User();
            user2.setEmail("admin");
            user2.setActive(true);
            user2.setPassword(passwordEncoder.encode("pass"));
            user2.setRoles(Arrays.asList(new Role("ROLE_ADMIN"),new Role("ROLE_SELLER")));

            userRepository.save(user1);
            userRepository.save(user2);
            System.out.println("Total users saved::"+userRepository.count());

        }
    }
}
