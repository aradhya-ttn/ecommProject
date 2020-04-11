package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.validator.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class SellerRegisterDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    @ValidPassword
    private String password;
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String companyContactNo;
    @Pattern(regexp ="\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}")
    private String gst;
    private String companyName;

    public SellerRegisterDto(@Email String email, String password,
                             @Pattern(regexp = "(^$|[0-9]{10})") String companyContactNo, String gst, String companyName) {
        this.email = email;
        this.password = password;
        this.companyContactNo = companyContactNo;
        this.gst = gst;
        this.companyName = companyName;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyContactNo() {
        return companyContactNo;
    }

    public void setCompanyContactNo(String companyContactNo) {
        this.companyContactNo = companyContactNo;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
//    Format details
//
//         First 2 digits of the GST Number will represent State Code as per the Census (2011).
//         Next 10 digits will be same as in the PAN number of the taxpayer.
//           First five will be alphabets
//           Next four will be numbers
//           Last will be check code
//        The 13th digit will be the number of registration you take within a state i.e. after 9, A to Z is considered as 10 to 35 .
//        14th digit will be Z by default.
//        Last would be the check code.