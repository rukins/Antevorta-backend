package com.inventorymanagementsystem.exception;

import lombok.Getter;

@Getter
public class MissedFirstOrLastNameException extends Exception{
    private String path;
    public MissedFirstOrLastNameException(String message) {
        super(message);
    }

    public MissedFirstOrLastNameException(String message, String path) {
        super(message);

        this.path = path;
    }
}
