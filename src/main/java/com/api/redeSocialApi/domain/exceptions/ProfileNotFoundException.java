package com.api.redeSocialApi.domain.exceptions;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String msg){
        super(msg);
    }
}
