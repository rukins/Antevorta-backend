package com.inventorymanagementsystem.exception.globalexception;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String message, String path) {
        super(message, path);
    }
}
