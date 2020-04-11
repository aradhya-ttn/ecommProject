package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Seller;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepository  extends CrudRepository<Seller,Integer> {

    Seller findByGst(String gst);
    Seller findByUserId(Long id);
}
