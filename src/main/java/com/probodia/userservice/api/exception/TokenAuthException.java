package com.probodia.userservice.api.exception;

public class TokenAuthException extends RuntimeException{
    public TokenAuthException(String message) {
        super(message);
    }
}
