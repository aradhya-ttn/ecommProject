package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne(mappedBy = "orders",cascade = CascadeType.ALL)
    private OrderProduct orderProduct;

    @OneToOne(mappedBy = "orders",cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "customerUserID")
    private Customer customer;

   private Integer amountPaid;

   private Date dateCreated;

   private String paymentMethod;

   private String customerAddressAddressLine;

   private String customerAddressCity;

   private String customerAddressState;

   private String customerAddressCountry;

   private String customerAddressLabel;

   private Integer customerAddressZipCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerAddressAddressLine() {
        return customerAddressAddressLine;
    }

    public void setCustomerAddressAddressLine(String customerAddressAddressLine) {
        this.customerAddressAddressLine = customerAddressAddressLine;
    }

    public String getCustomerAddressCity() {
        return customerAddressCity;
    }

    public void setCustomerAddressCity(String customerAddressCity) {
        this.customerAddressCity = customerAddressCity;
    }

    public String getCustomerAddressState() {
        return customerAddressState;
    }

    public void setCustomerAddressState(String customerAddressState) {
        this.customerAddressState = customerAddressState;
    }

    public String getCustomerAddressCountry() {
        return customerAddressCountry;
    }

    public void setCustomerAddressCountry(String customerAddressCountry) {
        this.customerAddressCountry = customerAddressCountry;
    }

    public String getCustomerAddressLabel() {
        return customerAddressLabel;
    }

    public void setCustomerAddressLabel(String customerAddressLabel) {
        this.customerAddressLabel = customerAddressLabel;
    }

    public Integer getCustomerAddressZipCode() {
        return customerAddressZipCode;
    }

    public void setCustomerAddressZipCode(Integer customerAddressZipCode) {
        this.customerAddressZipCode = customerAddressZipCode;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customer=" + customer +
                ", amountPaid=" + amountPaid +
                ", dateCreated=" + dateCreated +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerAddressAddressLine='" + customerAddressAddressLine + '\'' +
                ", customerAddressCity='" + customerAddressCity + '\'' +
                ", customerAddressState='" + customerAddressState + '\'' +
                ", customerAddressCountry='" + customerAddressCountry + '\'' +
                ", customerAddressLabel='" + customerAddressLabel + '\'' +
                ", customerAddressZipCode=" + customerAddressZipCode +
                '}';
    }
}
