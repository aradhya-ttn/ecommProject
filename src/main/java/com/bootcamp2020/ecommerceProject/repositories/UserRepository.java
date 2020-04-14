package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Integer> {

    User findByEmail(String email);
    User findById(Long id);

    @Query(value = "select id,first_name,middle_name,last_name,email,is_active,image_path from user where id in (select user_id from customer )",nativeQuery = true)
    public List<Object[]> allCustomers(PageRequest pageRequest);

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active,s.company_contact,s.company_name,s.gst from user u inner join seller s on  u.id=s.user_id",nativeQuery = true)
    public List<Object[]> allSellers(PageRequest pageRequest);

    @Transactional
    @Modifying
    @Query(value = "update user set password= :Password where email=:Email",nativeQuery = true)
    public void updatePassword(@Param("Password") String password, @Param("Email") String email);

    @Query(value="select u.id ,u.first_name,u.last_name,u.is_active,u.image_path,c.contact from user u inner join customer c on u.id=c.user_id where email=:Email",nativeQuery = true)
    public List<Object[]> customerProfile(@Param("Email") String email);

}
