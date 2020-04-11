package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.entities.VerificationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

public interface VerificationTokenRepository  extends CrudRepository<VerificationToken,Long> {

        VerificationToken findByToken(String token);

        VerificationToken findByUser(User user);


        @Transactional
        @Modifying
        @Query(value = "update verification_token set token= :Token,expiry_date = :ExpDate where user_id=:Id",nativeQuery = true)
        public void doUpdateInfo(@Param("Token") String token, @Param("ExpDate") Date date, @Param("Id") Long id);

        @Transactional
        @Modifying
        @Query(value = "delete from verification_token where token =:Token",nativeQuery = true)
        public void dodeleteActivateToken(@Param("Token") String token);

}
