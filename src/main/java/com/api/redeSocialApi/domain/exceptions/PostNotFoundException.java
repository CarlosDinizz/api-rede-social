package com.api.redeSocialApi.domain.exceptions;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message) {
        super(message);
    }
}
