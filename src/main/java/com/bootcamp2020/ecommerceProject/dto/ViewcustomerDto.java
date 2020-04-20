package com.bootcamp2020.ecommerceProject.dto;

import com.bootcamp2020.ecommerceProject.utils.HashMapConverter;

import javax.persistence.Convert;
import java.util.Map;

public class ViewcustomerDto {

    private Long variationId;

    private String primageImage;

    @Convert(converter = HashMapConverter.class)
    private Map<String, String> metadata;

    public Long getVariationId() {
        return variationId;
    }

    public void setVariationId(Long variationId) {
        this.variationId = variationId;
    }

    public String getPrimageImage() {
        return primageImage;
    }

    public void setPrimageImage(String primageImage) {
        this.primageImage = primageImage;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
