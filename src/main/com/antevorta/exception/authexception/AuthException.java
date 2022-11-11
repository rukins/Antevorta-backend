package com.antevorta.exception.authexception;

import lombok.Getter;

@Getter
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}
