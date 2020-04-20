package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long parentId;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private Set<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private Set<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<CategoryMetadataFieldValues> getCategoryMetadataFieldValuesList() {
        return categoryMetadataFieldValuesList;
    }

    public void setCategoryMetadataFieldValuesList(Set<CategoryMetadataFieldValues> categoryMetadataFieldValuesList) {
        this.categoryMetadataFieldValuesList = categoryMetadataFieldValuesList;
    }
}
