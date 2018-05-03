package com.sec.cryptohds.service.exceptions;

public class LedgerDoesNotExistException extends CryptohdsException {

	private static final long serialVersionUID = 7081824645068663788L;

	public LedgerDoesNotExistException(String publicKey) {
		super("Ledger with Public Key: " + publicKey + " does not exists!");
	}
}
