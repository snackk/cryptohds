package com.sec.cryptohds.service.exceptions;

public class SequenceNumberNoMatchException extends CryptohdsException {

    public SequenceNumberNoMatchException() {
        super("Ledger sequence number doesn't match with server's!");
    }
}
