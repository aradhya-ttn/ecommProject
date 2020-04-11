package com.bootcamp2020.ecommerceProject.dto;

import java.math.BigInteger;

public class AllCustomerDto {

    private BigInteger id;
    private String fullName;
    private String email;
    private Boolean isActive;

    public AllCustomerDto(BigInteger id, String fullName, String email, Boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.isActive = isActive;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
