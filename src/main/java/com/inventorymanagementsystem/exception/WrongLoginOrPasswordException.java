package com.inventorymanagementsystem.exception;

public class WrongLoginOrPasswordException extends Exception{
    public WrongLoginOrPasswordException(String message) {
        super(message);
    }
}
