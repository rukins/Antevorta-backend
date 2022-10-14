package com.inventorymanagementsystem.exception.globalexception;

public class MissedFirstOrLastNameException extends GlobalException {
    public MissedFirstOrLastNameException(String message, String path) {
        super(message, path);
    }
}
