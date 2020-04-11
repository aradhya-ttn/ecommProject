package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.ForgotPasswordToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ForgotPasswordTokenRepository extends CrudRepository<ForgotPasswordToken,Long> {

    ForgotPasswordToken findByToken(String Token);

    @Transactional
    @Modifying
    @Query(value = "delete from forgot_password_token where token =:Token",nativeQuery = true)
    public void   deleteToken(@Param("Token") String token);

    @Transactional
    @Modifying
    @Query(value = "delete from forgot_password_token where user_id =:Id",nativeQuery = true)
    public void   deleteTokenById(@Param("Id") Long id);


}
