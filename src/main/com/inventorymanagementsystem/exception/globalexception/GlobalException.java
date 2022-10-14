package com.inventorymanagementsystem.exception.globalexception;

import lombok.Getter;

@Getter
public class GlobalException extends Exception {
    private final String path;

    public GlobalException(String message, String path) {
        super(message);

        this.path = path;
    }
}
