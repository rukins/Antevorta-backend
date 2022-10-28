package com.inventorymanagementsystem.exception.authexception;

public class WrongPasswordException extends AuthException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
