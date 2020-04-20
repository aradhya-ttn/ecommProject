package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Long> {

    List<ProductVariation> findByProductId(Long productId);

    @Query(value = "select * from product_variation where product_id =:productId",nativeQuery = true)
    public List<ProductVariation> getAllVariationByProductId(@Param("productId") Long parentId, Pageable pageable);
}
