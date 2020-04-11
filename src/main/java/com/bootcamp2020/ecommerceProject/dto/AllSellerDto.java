package com.bootcamp2020.ecommerceProject.dto;

import java.math.BigInteger;

public class AllSellerDto {

        private BigInteger id;
        private String fullName;
        private String email;
        private Boolean isActive;
        private String companyContact;
        private String companyName;
        private  String gst;

    public AllSellerDto(BigInteger id, String fullName, String email, Boolean isActive, String companyContact, String companyName, String gst) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.isActive = isActive;
        this.companyContact = companyContact;
        this.companyName = companyName;
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
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

