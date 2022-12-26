package com.antevorta.security.exception.authexception;

public class WrongPasswordException extends AuthorizationException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
