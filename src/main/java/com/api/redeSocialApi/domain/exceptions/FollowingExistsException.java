package com.api.redeSocialApi.domain.exceptions;

public class FollowingExistsException extends RuntimeException{
    public FollowingExistsException(String message) {
        super(message);
    }
}
