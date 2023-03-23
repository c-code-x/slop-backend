package com.slop.slopbackend.exception;

import com.slop.slopbackend.dto.response.exception.ExceptionResDTO;
import com.slop.slopbackend.dto.response.exception.MethodArgumentNotValidExceptionResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiRuntimeException.class)
    public ResponseEntity<ExceptionResDTO> exceptionHandler(ApiRuntimeException exception, WebRequest request){
        ExceptionResDTO response= ExceptionResDTO.builder()
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .timestamp(new Date())
                .httpStatus(exception.getHttpStatus())
                .build();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResDTO> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, WebRequest request){
        Map<String,Object> errors=new HashMap<>();
        exception.getAllErrors().forEach((error)->{
            System.out.println(error);
            errors.put(((FieldError)error).getField(),error.getDefaultMessage());
        });
        MethodArgumentNotValidExceptionResDTO response= MethodArgumentNotValidExceptionResDTO.builder()
                .message("Invalid data")
                .errors(errors)
                .timestamp(new Date())
                .path(request.getDescription(false))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response,response.getHttpStatus());
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResDTO> missingRequestParamExceptionHandler(Exception exception, WebRequest request){
        ExceptionResDTO response= ExceptionResDTO.builder()
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .timestamp(new Date())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResDTO> globalExceptionHandler(Exception exception, WebRequest request){
        ExceptionResDTO response= ExceptionResDTO.builder()
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .timestamp(new Date())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}