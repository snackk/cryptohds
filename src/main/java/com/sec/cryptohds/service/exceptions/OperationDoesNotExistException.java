package com.sec.cryptohds.service.exceptions;

public class OperationDoesNotExistException extends CryptohdsException {

	private static final long serialVersionUID = 5551740073691699411L;

	public OperationDoesNotExistException(Long operationID) {
        super("Operation with ID: " + operationID + ", does not exist!");
    }
}
