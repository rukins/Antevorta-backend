package com.inventorymanagementsystem.exception.globalexception;

public class EntityNotFoundException extends GlobalException {
    public EntityNotFoundException(String message, String path) {
        super(message, path);
    }
}
