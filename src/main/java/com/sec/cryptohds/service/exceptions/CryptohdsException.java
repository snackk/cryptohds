package com.sec.cryptohds.service.exceptions;

public class CryptohdsException extends RuntimeException {

	private static final long serialVersionUID = -5782147525992104364L;

	private String message;

    public CryptohdsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
