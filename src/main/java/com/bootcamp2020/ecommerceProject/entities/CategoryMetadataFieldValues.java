package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;

@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMetadataFieldAndValues categoryMetadataFieldAndValues;

    @MapsId("categoryMetadataFieldId")
    @ManyToOne
    private CategoryMetadataField categoryMetadataField;

    @MapsId("categoryId")
    @ManyToOne
    private  Category category;

    private String value;

    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CategoryMetadataFieldAndValues getCategoryMetadataFieldAndValues() {
        return categoryMetadataFieldAndValues;
    }

    public void setCategoryMetadataFieldAndValues(CategoryMetadataFieldAndValues categoryMetadataFieldAndValues) {
        this.categoryMetadataFieldAndValues = categoryMetadataFieldAndValues;
    }
}
