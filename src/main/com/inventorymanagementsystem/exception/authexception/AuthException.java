package com.inventorymanagementsystem.exception.authexception;

import lombok.Getter;

@Getter
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}
