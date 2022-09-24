package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.model.ResponseMessageBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({WrongLoginOrPasswordException.class})
    public ResponseEntity<?> handleWrongLoginOrPasswordException(Exception ex) {
        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

        return new ResponseEntity<>(messageBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MissedFirstOrLastNameException.class})
    public ResponseEntity<?> handleMissedFirstOrLastNameException(Exception ex) {
        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(messageBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IncorrectEmailException.class})
    public ResponseEntity<?> handleIncorrectEmailException(Exception ex) {
        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(messageBody, HttpStatus.BAD_REQUEST);
    }
}
