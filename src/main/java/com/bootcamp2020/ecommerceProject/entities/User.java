package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<Address> addresses;
//
//    public List<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<Address> addresses) {
//        this.addresses = addresses;
//    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Embedded
    private Name name;
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name = "UserRole",joinColumns=@JoinColumn( name = "UserId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "RoleId",referencedColumnName = "id"))
    private List<Role> roles;


    private String password;
    private String imagePath;
    private Boolean isDeleted = false;
    private Boolean isActive = false;
    private Boolean isAccountNonLocked=true;


    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Seller seller;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }
}
