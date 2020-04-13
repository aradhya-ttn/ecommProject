package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.UserAttempts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserAttemptsRepository extends CrudRepository<UserAttempts,Long> {

    UserAttempts findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update user_attempts set attempts =:Attempts where email=:Email",nativeQuery = true)
    public void updateAttempts(@Param("Attempts") Integer attempts, @Param("Email") String email);

}
