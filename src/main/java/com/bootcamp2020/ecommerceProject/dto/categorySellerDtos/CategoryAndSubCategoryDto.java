package com.bootcamp2020.ecommerceProject.dto.categorySellerDtos;

import java.util.List;

public class CategoryAndSubCategoryDto {

    private Long categoryId;

    private String categoryName;

    private List<String> parentName;

    private List<MetadatafieldNameAndValuesDto> metadatafieldNameAndValues;

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

    public List<String> getParentName() {
        return parentName;
    }

    public void setParentName(List<String> parentName) {
        this.parentName = parentName;
    }

    public List<MetadatafieldNameAndValuesDto> getMetadatafieldNameAndValues() {
        return metadatafieldNameAndValues;
    }

    public void setMetadatafieldNameAndValues(List<MetadatafieldNameAndValuesDto> metadatafieldNameAndValues) {
        this.metadatafieldNameAndValues = metadatafieldNameAndValues;
    }
}
