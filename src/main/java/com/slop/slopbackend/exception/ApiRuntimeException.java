package com.slop.slopbackend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiRuntimeException extends RuntimeException {
    private HttpStatus httpStatus;

    public ApiRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
