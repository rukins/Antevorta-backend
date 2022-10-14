package com.inventorymanagementsystem.exception.globalexception;

public class MultipleOnlineStoresException extends GlobalException {
    public MultipleOnlineStoresException(String message, String path) {
        super(message, path);
    }
}
