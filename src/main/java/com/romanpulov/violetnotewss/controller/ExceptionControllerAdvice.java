package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({PassDataFileNotFoundException.class, PassDataFileReadException.class})
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
