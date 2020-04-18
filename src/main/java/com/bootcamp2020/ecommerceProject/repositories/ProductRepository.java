package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product,Long> {

    Product findByid(Long id);

    @Query(value = "select * from product where brand =:Brand and category_id =:categoryId and name =:Name and seller_user_id=:userId",nativeQuery = true)
    public Product getProoductByName(@Param("Brand") String brand,@Param("categoryId") Long categoryId,@Param("Name") String name,@Param("userId") Long userId);
}
