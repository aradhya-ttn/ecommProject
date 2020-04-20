package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Long> {

    Product findByid(Long id);

    @Query(value = "select * from product where brand =:Brand and category_id =:categoryId and name =:Name and seller_user_id=:userId",nativeQuery = true)
    public Product getProductByName(@Param("Brand") String brand,@Param("categoryId") Long categoryId,@Param("Name") String name,@Param("userId") Long userId);

    @Query(value = "select * from product where seller_user_id=:sellerId and is_deleted=false",nativeQuery = true)
    public List<Product> getAllProduct(@Param("sellerId") Long sellerId, Pageable pageable);

    List<Product>  findByCategoryId(Long categoryId, Pageable pageable);

    @Query(value = "select * from product where category_id=:categoryId",nativeQuery = true)
    public List<Product> getProduct(@Param("categoryId")Long categoryId);

}
