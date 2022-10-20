package com.inventorymanagementsystem.exception.authexception;

public class WrongLoginOrPasswordException extends AuthException {
    public WrongLoginOrPasswordException(String message) {
        super(message);
    }
}
