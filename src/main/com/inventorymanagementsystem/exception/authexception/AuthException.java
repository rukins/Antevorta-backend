package com.inventorymanagementsystem.exception.authexception;

import lombok.Getter;

@Getter
public class AuthException extends Exception {
    private final String path;

    public AuthException(String message, String path) {
        super(message);

        this.path = path;
    }
}
