package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.entities.Address;
import com.bootcamp2020.ecommerceProject.enums.Label;
import com.bootcamp2020.ecommerceProject.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressDao {

    @Autowired
    AddressRepository addressRepository;

    public Address getAddress(Integer userid){
      List<Object[]> addresslist= addressRepository.getAddress(userid);
            Address address = new Address();
        for (Object[] objects:addresslist) {
            address.setId((Long) objects[0]);
            address.setAddress(objects[1].toString());
            address.setCity(objects[2].toString());
            address.setState(objects[3].toString());
            address.setCountry(objects[4].toString());
            address.setLabel((Label) objects[5]);
            address.setZipCode((Integer) objects[5]);
        }
        return address;
    }
}
