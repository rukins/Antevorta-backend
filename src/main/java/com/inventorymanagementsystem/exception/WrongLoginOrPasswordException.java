package com.inventorymanagementsystem.exception;

import lombok.Getter;

@Getter
public class WrongLoginOrPasswordException extends Exception {
    private String path;
    public WrongLoginOrPasswordException(String message) {
        super(message);
    }

    public WrongLoginOrPasswordException(String message, String path) {
        super(message);

        this.path = path;
    }
}
