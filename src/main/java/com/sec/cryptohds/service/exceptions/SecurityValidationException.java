package com.sec.cryptohds.service.exceptions;

public class SecurityValidationException extends CryptohdsException {

    public SecurityValidationException(String publicKey) {
        super("Envelope validation failed for Ledger with public key: " + publicKey);
    }
}
