package com.antevorta.exception.serverexception;

public class WrongVerificationCodeException extends ServerException {
    public WrongVerificationCodeException(String message) {
        super(message);
    }
}
