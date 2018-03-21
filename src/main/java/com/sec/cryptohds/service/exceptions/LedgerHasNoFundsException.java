package com.sec.cryptohds.service.exceptions;

public class LedgerHasNoFundsException extends CryptohdsException {

    public LedgerHasNoFundsException(String publicKey) {
        super("Ledger with Public Key: " + publicKey + " has no funds to create Operation!");
    }
}
