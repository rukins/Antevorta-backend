package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.exception.authexception.AuthException;
import com.inventorymanagementsystem.exception.globalexception.GlobalException;
import com.inventorymanagementsystem.model.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthException.class})
    public ResponseEntity<?> handleWrongLoginOrPasswordException(AuthException ex) {
        ResponseBody messageBody = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ex.getPath());

        return new ResponseEntity<>(messageBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({GlobalException.class})
    public ResponseEntity<?> handleGlobalException(GlobalException ex) {
        ResponseBody messageBody = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getPath());

        return new ResponseEntity<>(messageBody, HttpStatus.BAD_REQUEST);
    }
}
