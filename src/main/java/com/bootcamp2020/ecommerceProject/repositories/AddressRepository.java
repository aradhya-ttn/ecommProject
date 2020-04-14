package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Long> {


    @Query(value = "select a.id, a.address, a.city, a.state, a.country, a.label,a.zip_code from address a where customer_user_id = :id",nativeQuery = true)
    public List<Object[]> getAddress(@Param("id") Long id);

    @Query(value = "select * from address  where customer_user_id = :id",nativeQuery = true)
    public List<Address> getCustomerAddress(@Param("id") Long id);

    @Query(value = "select * from address where customer_user_id=:CustomerId and id=:Id",nativeQuery = true)
    public Address getAddressByadddressIdAndCustomerId(@Param("CustomerId")Long CustomerId,@Param("Id") Long id);

    @Query(value = "select * from address where seller_id=:SellerId and id=:Id",nativeQuery = true)
    public Address getAddressByadddressIdAndSellerId(@Param("SellerId")Long sellerId,@Param("Id") Long id);
}
