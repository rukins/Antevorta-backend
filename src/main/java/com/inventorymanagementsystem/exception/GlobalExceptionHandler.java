package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.model.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({WrongLoginOrPasswordException.class})
    public ResponseEntity<?> handleWrongLoginOrPasswordException(WrongLoginOrPasswordException ex) {
        ResponseBody messageBody = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ex.getPath());

        return new ResponseEntity<>(messageBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MissedFirstOrLastNameException.class})
    public ResponseEntity<?> handleMissedFirstOrLastNameException(MissedFirstOrLastNameException ex) {
        ResponseBody messageBody = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getPath());

        return new ResponseEntity<>(messageBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IncorrectEmailException.class})
    public ResponseEntity<?> handleIncorrectEmailException(IncorrectEmailException ex) {
        ResponseBody messageBody = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getPath());

        return new ResponseEntity<>(messageBody, HttpStatus.BAD_REQUEST);
    }
}
