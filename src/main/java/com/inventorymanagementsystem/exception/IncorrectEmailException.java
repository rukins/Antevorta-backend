package com.inventorymanagementsystem.exception;

import lombok.Getter;

@Getter
public class IncorrectEmailException extends Exception{
    private String path;
    public IncorrectEmailException(String message) {
        super(message);
    }

    public IncorrectEmailException(String message, String path) {
        super(message);

        this.path = path;
    }
}
