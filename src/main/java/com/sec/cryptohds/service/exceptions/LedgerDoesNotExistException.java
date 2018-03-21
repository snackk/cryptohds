package com.sec.cryptohds.service.exceptions;

public class LedgerDoesNotExistException extends CryptohdsException {

    public LedgerDoesNotExistException() {
    	super("Ledger does not exists!");
	}
}
