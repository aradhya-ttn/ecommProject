package com.bootcamp2020.ecommerceProject.dto;

import java.util.List;

public class CategoryDto {

    private Long id;

    private List<String> parentCategory;

    private String subCategory;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(List<String> parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
