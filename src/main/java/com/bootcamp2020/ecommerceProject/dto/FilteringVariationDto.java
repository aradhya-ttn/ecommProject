package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.utils.HashMapConverter;

import javax.persistence.Convert;
import java.util.Map;

public class FilteringVariationDto {

    private Double price;

    @Convert(converter = HashMapConverter.class)
    private Map<String, String> metadata;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
