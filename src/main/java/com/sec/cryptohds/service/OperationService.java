package com.sec.cryptohds.service;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.repository.LedgerRepository;
import com.sec.cryptohds.repository.OperationRepository;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;
import com.sec.cryptohds.service.exceptions.LedgerHasNoFundsException;
import com.sec.cryptohds.service.exceptions.OperationDoesNotExistException;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.ReceiveOperationDTO;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;

    private final LedgerRepository ledgerRepository;

    public OperationService(OperationRepository operationRepository, LedgerRepository ledgerRepository) {
        this.operationRepository = operationRepository;
        this.ledgerRepository = ledgerRepository;
    }

	public void createOperation(OperationDTO operationDTO, byte[] signature) throws CryptohdsException {
        Ledger origin = ledgerRepository.findLedgerByPublicKey(operationDTO.getOriginPublicKey());
        Ledger destination = ledgerRepository.findLedgerByPublicKey(operationDTO.getDestinationPublicKey());

        if(origin == null) {
            throw new LedgerDoesNotExistException(operationDTO.getOriginPublicKey());
        }
        if(destination == null) {
            throw new LedgerDoesNotExistException(operationDTO.getDestinationPublicKey());
        }

		// get all operations
		List<Operation> operations = findOperations();

        // calculate ledger debt
        Long ledgerDebt = 0L;
		for (Operation op : operations) {
			if (!op.isCommitted()) {
				if (origin.equals(op.getOriginLedger())) {
            		ledgerDebt += op.getValue();
				} else if (origin.equals(op.getDestinationLedger())) {
            		ledgerDebt -= op.getValue();
            	}
			}
        }
 
        if(origin.getBalance() <= operationDTO.getValue() || origin.getBalance() <= ledgerDebt) {
            throw new LedgerHasNoFundsException(origin.getPublicKey());
        }

		Operation operation = new Operation(origin, destination, operationDTO.getValue());

		// set origin signature with the operation
		operation.setOriginSignature(Base64.getEncoder().encodeToString(signature));

		// save operation
		operation = saveOperation(operation);
    }

    public void receiveOperation(ReceiveOperationDTO receiveOperationDTO, byte[] signature) throws OperationDoesNotExistException {
        Ledger destinationLedger = ledgerRepository.findLedgerByPublicKey(receiveOperationDTO.getPublicKey());
        Operation operation = findOperationById(receiveOperationDTO.getOperationId());

		if (operation == null) {
            throw new OperationDoesNotExistException(receiveOperationDTO.getOperationId());
        }

		// update destination ledger balance
    	destinationLedger.setBalance(destinationLedger.getBalance() + operation.getValue());

		// set origin ledger new balance
		Ledger originLeder = ledgerRepository.findLedgerByPublicKey(operation.getOriginLedger().getPublicKey());
		originLeder.setBalance(originLeder.getBalance() - operation.getValue());
		ledgerRepository.save(originLeder);

		// set operation to committed and add message's signature
		operation.setCommitted(true);
		operation.setDestinationSignature(Base64.getEncoder().encodeToString(signature));
		saveOperation(operation);

		// persist destination ledger changes
		ledgerRepository.save(destinationLedger);
    }

	public Operation saveOperation(Operation op) {
		return operationRepository.save(op);
    }

	public List<Operation> findOperations() {
		return operationRepository.findAll();
	}

	public Operation findOperationById(Long id) {
		return operationRepository.findOperationById(id);
	}
}
