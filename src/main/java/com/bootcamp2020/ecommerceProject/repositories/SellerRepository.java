package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerRepository  extends CrudRepository<Seller,Integer> {

    Seller findByGst(String gst);
    Seller findByUserId(Long id);

    @Query(value = "select s.user_id, u.first_name,u.last_name,u.is_active,u.image_path,s.company_contact,s.company_name,s.gst,a.address,a.city,a.country,a.state,a.zip_code from user u,seller s,address a where u.id=s.user_id and s.user_id=a.seller_id and user_id=:Id ",nativeQuery = true)
    public List<Object[]> sellerProfile(@Param("Id") Long id);
}
