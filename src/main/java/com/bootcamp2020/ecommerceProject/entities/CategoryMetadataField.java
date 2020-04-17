package com.bootcamp2020.ecommerceProject.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categoryMetadataField",cascade = CascadeType.ALL)
    private Set<CategoryMetadataFieldValues> categoryMetadataFieldValues;

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

//    public Set<CategoryMetadataFieldValues> getCategoryMetadataFieldValues() {
//        return categoryMetadataFieldValues;
//    }
//
//    public void setCategoryMetadataFieldValues(Set<CategoryMetadataFieldValues> categoryMetadataFieldValues) {
//        this.categoryMetadataFieldValues = categoryMetadataFieldValues;
//    }

    @Override
    public String toString() {
        return "CategoryMetadataField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
