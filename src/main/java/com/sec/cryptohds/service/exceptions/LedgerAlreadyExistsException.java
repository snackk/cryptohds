package com.sec.cryptohds.service.exceptions;

public class LedgerAlreadyExistsException extends CryptohdsException {

    public LedgerAlreadyExistsException(String publicKey) {
        super("Ledger with Public Key: " + publicKey + " already exists!");
    }
}
