package com.api.redeSocialApi.domain.exceptions;

public class UserUnauthorizedException extends RuntimeException{

    public UserUnauthorizedException() {
        super("User does not have permission");
    }

    public UserUnauthorizedException(String message) {
        super(message);
    }
}
