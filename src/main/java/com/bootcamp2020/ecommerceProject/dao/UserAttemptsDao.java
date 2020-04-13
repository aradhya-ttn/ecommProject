package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.entities.User;
import com.bootcamp2020.ecommerceProject.entities.UserAttempts;
import com.bootcamp2020.ecommerceProject.repositories.UserAttemptsRepository;
import com.bootcamp2020.ecommerceProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Calendar;

@Component
public class UserAttemptsDao {



    @Autowired
    private UserAttemptsRepository userAttemptsRepository;
    @Autowired
    private UserRepository userRepository;

    public UserAttempts getUserAttempts(String email){
        UserAttempts userAttempts=userAttemptsRepository.findByEmail(email);
        if(userAttempts!=null){
            return userAttempts;

        }
        else{
             Integer count=0;
            Date date= new Date(Calendar.getInstance().getTime().getTime());
            UserAttempts userAttempts1= new UserAttempts();
            userAttempts1.setEmail(email);
            userAttempts1.setAttempts(count);
            userAttempts1.setLastModified(date);
            userAttemptsRepository.save(userAttempts1);
            return userAttemptsRepository.findByEmail(email);

        }

    }
    public  Integer getAttempts(String email){
        UserAttempts userAttempts=userAttemptsRepository.findByEmail(email);
        Integer attempts=userAttempts.getAttempts();
        return attempts;
    }
     public Boolean checkAccountLock(String email){
        Integer attempts= getAttempts(email);
        if(attempts>3){
           return false;
        }else{
            return true;
        }
    }
    public Boolean checkIsActive(String email){
        User user =userRepository.findByEmail(email);
        Boolean active = user.getActive();
        return active;
    }


}
