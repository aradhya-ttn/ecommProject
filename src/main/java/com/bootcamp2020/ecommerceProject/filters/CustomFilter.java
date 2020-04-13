package com.bootcamp2020.ecommerceProject.filters;

import com.bootcamp2020.ecommerceProject.dao.UserAttemptsDao;
import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.entities.UserAttempts;
import com.bootcamp2020.ecommerceProject.exceptions.UserNotFoundException;
import com.bootcamp2020.ecommerceProject.repositories.UserAttemptsRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomFilter  extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAttemptsDao userAttemptsDao;

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            Authentication authentication1= super.authenticate(authentication);
            String email=authentication.getName();
            User user=userRepository.findByEmail(email);
            if(user!=null){
                UserAttempts userAttempts=userAttemptsDao.getUserAttempts(email);
                Boolean checkLock=userAttemptsDao.checkAccountLock(email);
                Boolean isActive=userAttemptsDao.checkIsActive(email);
                if(checkLock && isActive){
                    Integer count=0;
                    userAttemptsRepository.updateAttempts(count,email);
                    return authentication1;
                }else{
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    throw  new UserNotFoundException("Account is Locked");
                }
            }

        }catch (Exception e){
            String email=authentication.getName();

            UserAttempts userAttempts= userAttemptsDao.getUserAttempts(email);
            Integer attempts=userAttempts.getAttempts()+1;
            userAttemptsRepository.updateAttempts(attempts,email);

            throw  new UsernameNotFoundException("user not found------");
        }
        return null;
    }
}
