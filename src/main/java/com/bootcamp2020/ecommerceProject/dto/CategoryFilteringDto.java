package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.MetadatafieldNameAndValuesDto;

import java.util.List;
import java.util.Set;

public class CategoryFilteringDto {

    private String categoryName;

    private Set<String> brandList;

   private List<MetadatafieldNameAndValuesDto> metadatafieldNameAndValues;

    public List<MetadatafieldNameAndValuesDto> getMetadatafieldNameAndValues() {
        return metadatafieldNameAndValues;
    }

    public void setMetadatafieldNameAndValues(List<MetadatafieldNameAndValuesDto> metadatafieldNameAndValues) {
        this.metadatafieldNameAndValues = metadatafieldNameAndValues;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<String> getBrandList() {
        return brandList;
    }

    public void setBrandList(Set<String> brandList) {
        this.brandList = brandList;
    }

}
