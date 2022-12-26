package com.antevorta.security.exception.authexception;

public class UserNotAuthenticatedException extends AuthenticationException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
