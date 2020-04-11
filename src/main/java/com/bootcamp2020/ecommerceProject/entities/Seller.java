package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String gst;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;


    private String companyContact;
    private String companyName;
    @MapsId
    @OneToOne
    @JoinColumn(name = "UserId")
    private  User user;

//    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
//    private List<Address> addresses;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public List<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<Address> addresses) {
//        this.addresses = addresses;
//    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
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
}
