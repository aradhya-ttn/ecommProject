package com.bootcamp2020.ecommerceProject.springSecurity;

import com.bootcamp2020.ecommerceProject.entities.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class GrantAuthorityImpl implements GrantedAuthority {

    String authority;

    public GrantAuthorityImpl(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}