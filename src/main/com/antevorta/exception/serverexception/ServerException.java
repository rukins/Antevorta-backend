package com.antevorta.exception.serverexception;

import lombok.Getter;

@Getter
public class ServerException extends Exception {
    public ServerException(String message) {
        super(message);
    }
}
