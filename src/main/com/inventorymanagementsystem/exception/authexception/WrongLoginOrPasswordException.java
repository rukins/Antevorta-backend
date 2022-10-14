package com.inventorymanagementsystem.exception.authexception;

public class WrongLoginOrPasswordException extends AuthException {
    public WrongLoginOrPasswordException(String message, String path) {
        super(message, path);
    }
}
