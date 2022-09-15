package com.slop.slopbackend.dto.response.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
public class ExceptionResDTO {
    private String message;
    private Date timestamp;
    private String path;
    private HttpStatus httpStatus;
}
