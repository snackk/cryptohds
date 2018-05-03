package com.sec.cryptohds.service.exceptions;

public class SecurityValidationException extends CryptohdsException {

	private static final long serialVersionUID = 274193433168121203L;

	public SecurityValidationException(String publicKey) {
        super("Envelope validation failed for Ledger with public key: " + publicKey);
    }
}
