package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;

@Entity
public class ProductReview {

    @Id
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CustomerUserId")
    private Orders orders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ProductId")
    private Product product;

    private String review;
    private  Integer rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
