package com.antevorta.security.exception.authexception;

public class UserNotAuthenticatedException extends AuthException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
