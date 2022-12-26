package com.antevorta.security.exception.authexception;

public class MultipleOnlineStoresByTypeException extends AuthorizationException {
    public MultipleOnlineStoresByTypeException(String message) {
        super(message);
    }
}
