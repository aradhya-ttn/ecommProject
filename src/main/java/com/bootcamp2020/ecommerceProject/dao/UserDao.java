package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.springSecurity.GrantAuthorityImpl;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import com.bootcamp2020.ecommerceProject.entities.Role;
import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.springSecurity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDao {

    @Autowired
    UserRepository userRepository;

    public AppUser loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        System.out.println(user);
        List<GrantAuthorityImpl> grantAuthorities =new ArrayList<>();
        List<Role> userRoles= user.getRoles();
        for (Role roles: userRoles) {
            String authority = roles.getAuthority();
            grantAuthorities.add(new GrantAuthorityImpl(authority));
        }

        if (username != null) {

                       return new AppUser(user.getEmail(), user.getPassword(),user.getAccountNonLocked(), grantAuthorities);
        } else {
            throw new RuntimeException();
        }

    }
}
