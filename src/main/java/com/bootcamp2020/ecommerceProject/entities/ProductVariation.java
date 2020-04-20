package com.bootcamp2020.ecommerceProject.entities;

import com.bootcamp2020.ecommerceProject.utils.HashMapConverter;

import javax.persistence.*;
import java.util.Map;

@Entity
public class  ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    private Integer quantityAvailable=0;
    private Double price=0.0;

    @Convert(converter = HashMapConverter.class)
    private Map<String,String> metadata;

    private String primaryImageName;

    private String secondaryImageName;

    private Boolean isActive=true;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Map<String, String > getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getPrimaryImageName() {
        return primaryImageName;
    }

    public void setPrimaryImageName(String primaryImageName) {
        this.primaryImageName = primaryImageName;
    }

    public String getSecondaryImageName() {
        return secondaryImageName;
    }

    public void setSecondaryImageName(String secondaryImageName) {
        this.secondaryImageName = secondaryImageName;
    }
}
