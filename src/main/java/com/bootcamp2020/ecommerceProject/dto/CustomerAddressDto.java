package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.enums.Label;

public class CustomerAddressDto {


    private String city;
    private String state;
    private String country;
    private String address;
    private Label label;
    private Integer zipcode;

    public CustomerAddressDto(String city, String state, String country, String address, Label label, Integer zipcode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.label = label;
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}
