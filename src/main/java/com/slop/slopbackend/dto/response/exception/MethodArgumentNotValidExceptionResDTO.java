package com.slop.slopbackend.dto.response.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Data
@Builder
public class MethodArgumentNotValidExceptionResDTO {
    private String message;
    private Date timestamp;
    private String path;
    private HttpStatus httpStatus;
    private Map<String,Object> errors;
}
