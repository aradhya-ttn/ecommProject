package com.bootcamp2020.ecommerceProject.dto;

import java.util.Set;

public class CategoryMetadatfieldValuesDto {

    private Long categoryId;
    private Long metadataFieldId;

    private Set<String> fieldValues;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getMetadataFieldId() {
        return metadataFieldId;
    }

    public void setMetadataFieldId(Long metadataFieldId) {
        this.metadataFieldId = metadataFieldId;
    }

    public Set<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Set<String> fieldValues) {
        this.fieldValues = fieldValues;
    }
}