package com.bootcamp2020.ecommerceProject.filters;

import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomFilter  extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Authentication authentication1= super.authenticate(authentication);
            User user=userRepository.findByEmail(authentication1.getName());
            return authentication1;
        }catch ()
    }
}
