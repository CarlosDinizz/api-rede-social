package com.api.redeSocialApi.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {

    private HttpStatus status;
    private String error;
}
