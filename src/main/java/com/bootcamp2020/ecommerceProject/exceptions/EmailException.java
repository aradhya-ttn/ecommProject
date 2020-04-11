package com.bootcamp2020.ecommerceProject.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailException extends RuntimeException{

    public EmailException(String message) {
        super(message);
    }
}
