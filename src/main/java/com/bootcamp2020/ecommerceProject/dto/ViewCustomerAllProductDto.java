package com.bootcamp2020.ecommerceProject.dto;

import java.util.List;

public class ViewCustomerAllProductDto {

    private Long productId;

    private String productName;

    private Long categoryId;

    private String categoryName;

    private List<ViewcustomerDto> variation;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ViewcustomerDto> getVariation() {
        return variation;
    }

    public void setVariation(List<ViewcustomerDto> variation) {
        this.variation = variation;
    }
}
