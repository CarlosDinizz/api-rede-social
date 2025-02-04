package com.api.redeSocialApi.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> profileNotFoundExceptionHandler(ProfileNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProfileUsernameException.class)
    public ResponseEntity<ExceptionResponse> profileUsernameExceptionHandler(ProfileUsernameException exception){
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(FollowingExistsException.class)
    public ResponseEntity<ExceptionResponse> followingExistsExceptionHandler(FollowingExistsException exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<ExceptionResponse> userEmailExceptionHandler(UserEmailException exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> commentNotFoundExceptionHandler(CommentNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ExceptionResponse> postNotFoundExceptionHandler(PostNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(UserNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
