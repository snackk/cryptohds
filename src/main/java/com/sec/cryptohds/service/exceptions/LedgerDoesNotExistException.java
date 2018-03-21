package com.sec.cryptohds.service.exceptions;

public class LedgerDoesNotExistException extends CryptohdsException {

    public LedgerDoesNotExistException(String publicKey) {
		super("Ledger with Public Key: " + publicKey + " does not exists!");
	}
}
