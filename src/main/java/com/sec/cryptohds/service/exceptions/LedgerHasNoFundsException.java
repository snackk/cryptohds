package com.sec.cryptohds.service.exceptions;

public class LedgerHasNoFundsException extends CryptohdsException {

	private static final long serialVersionUID = 6774775105567842407L;

	public LedgerHasNoFundsException(String publicKey) {
        super("Ledger with Public Key: " + publicKey + " has no funds to create Operation!");
    }
}
