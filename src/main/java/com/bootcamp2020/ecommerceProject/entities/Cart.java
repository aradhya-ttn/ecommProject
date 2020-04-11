package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;

@Entity
public class Cart {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "CustomerUserId")
    private  Orders orders;

    private Integer quantity;

    private Boolean isWishlistItem;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getWishlistItem() {
        return isWishlistItem;
    }

    public void setWishlistItem(Boolean wishlistItem) {
        isWishlistItem = wishlistItem;
    }
}
