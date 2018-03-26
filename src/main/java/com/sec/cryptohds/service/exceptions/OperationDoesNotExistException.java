package com.sec.cryptohds.service.exceptions;

public class OperationDoesNotExistException extends CryptohdsException {

    public OperationDoesNotExistException(Long operationID) {
        super("Operation with ID: " + operationID + ", does not exist!");
    }
}
