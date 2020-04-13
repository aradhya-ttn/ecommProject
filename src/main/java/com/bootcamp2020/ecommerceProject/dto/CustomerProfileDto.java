package com.bootcamp2020.ecommerceProject.dto;

import java.math.BigInteger;

public class CustomerProfileDto {

    private BigInteger Id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String  contact;
    private String image;

    public CustomerProfileDto(BigInteger id, String firstName, String lastName, Boolean isActive, String image,String contact) {
        Id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.contact = contact;
        this.image = image;
    }

    public CustomerProfileDto(String contact) {
        this.contact = contact;
    }

    public BigInteger getId() {

        return Id;
    }

    public void setId(BigInteger id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
