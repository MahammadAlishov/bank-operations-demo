package com.bank.operation.exception.handler;

import com.bank.operation.dto.response.ErrorResponse;
import com.bank.operation.exception.GeneralException;
import com.bank.operation.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(NotFoundException exception, WebRequest webRequest) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(GeneralException exception, WebRequest webRequest) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
