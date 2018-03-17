package com.sec.cryptohds.service.exceptions;

public class CryptohdsException extends RuntimeException {

    private String message;

    public CryptohdsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
