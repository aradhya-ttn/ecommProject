package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryMetadataFieldAndValues implements Serializable {

    private Long categoryId;
    private Long categoryMetadataFieldId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryMetadataFieldId() {
        return categoryMetadataFieldId;
    }

    public void setCategoryMetadataFieldId(Long categoryMetadataFieldId) {
        this.categoryMetadataFieldId = categoryMetadataFieldId;
    }
}
