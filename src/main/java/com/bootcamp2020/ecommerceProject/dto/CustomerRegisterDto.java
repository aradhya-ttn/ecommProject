package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.validator.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class CustomerRegisterDto {

    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    @ValidPassword
    private String password;
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String contactNo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public CustomerRegisterDto(@Email String email, String firstName, String lastName, String middleName,
                            String password, @Pattern(regexp = "(^$|[0-9]{10})") String contactNo) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.contactNo = contactNo;
    }
}
