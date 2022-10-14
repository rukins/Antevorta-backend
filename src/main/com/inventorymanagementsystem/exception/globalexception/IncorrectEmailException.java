package com.inventorymanagementsystem.exception.globalexception;

public class IncorrectEmailException extends GlobalException {
    public IncorrectEmailException(String message, String path) {
        super(message, path);
    }
}
