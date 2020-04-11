package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contact;

    @MapsId
    @OneToMany(mappedBy ="customer",cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "UserId")
    private  User user;

//    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
//    private List<Address> addresses;

//    public List<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<Address> addresses) {
//        this.addresses = addresses;
//    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
