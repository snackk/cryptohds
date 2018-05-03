package com.sec.cryptohds.service.exceptions;

public class LedgerAlreadyExistsException extends CryptohdsException {

	private static final long serialVersionUID = -1823464755003967736L;

	public LedgerAlreadyExistsException(String publicKey) {
        super("Ledger with Public Key: " + publicKey + " already exists!");
    }
}
