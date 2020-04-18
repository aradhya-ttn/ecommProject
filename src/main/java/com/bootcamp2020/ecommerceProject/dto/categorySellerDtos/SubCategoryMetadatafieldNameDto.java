package com.bootcamp2020.ecommerceProject.dto.categorySellerDtos;

import java.util.List;

public class SubCategoryMetadatafieldNameDto {

    private String subCategory;

    private List<MetadatafieldNameAndValuesDto> nameAndValuesDtoList;

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<MetadatafieldNameAndValuesDto> getNameAndValuesDtoList() {
        return nameAndValuesDtoList;
    }

    public void setNameAndValuesDtoList(List<MetadatafieldNameAndValuesDto> nameAndValuesDtoList) {
        this.nameAndValuesDtoList = nameAndValuesDtoList;
    }
}
