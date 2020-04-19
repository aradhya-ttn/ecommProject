package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.ProductVariation;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Long> {

    ProductVariation findByProductId(Long productId);
}
