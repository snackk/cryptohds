package com.sec.cryptohds.service.exceptions;

public class SequenceNumberNoMatchException extends CryptohdsException {

	private static final long serialVersionUID = 5711350418741078970L;

	public SequenceNumberNoMatchException() {
        super("Ledger sequence number doesn't match with server's!");
    }
}
