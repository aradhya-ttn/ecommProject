package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Integer> {

    @Query(value = "select a.id, a.address, a.city, a.state, a.country, a.label,a.zip_code from address a where customer_user_id = :id",nativeQuery = true)
    public List<Object[]> getAddress(@Param("id") Integer id);
}
