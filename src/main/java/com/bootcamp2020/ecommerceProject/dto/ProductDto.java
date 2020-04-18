package com.bootcamp2020.ecommerceProject.dto;

public class ProductDto {
    private String name;

    private String brand;

    private Long categoryId;

    private String description;

    private Boolean IsCancellable;

    private Boolean IsReturnable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCancellable() {
        return IsCancellable;
    }

    public void setCancellable(Boolean cancellable) {
        IsCancellable = cancellable;
    }

    public Boolean getReturnable() {
        return IsReturnable;
    }

    public void setReturnable(Boolean returnable) {
        IsReturnable = returnable;
    }
}
