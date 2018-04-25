package com.sec.cryptohds.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.domain.OperationType;
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

    private final LedgerService ledgerService;

    public OperationService(OperationRepository operationRepository, LedgerService ledgerService) {
        this.operationRepository = operationRepository;
        this.ledgerService = ledgerService;
    }

	public void createOperation(OperationDTO operationDTO, byte[] signature) throws CryptohdsException {
        Ledger origin = ledgerService.findLedgerByPublicKey(operationDTO.getOriginPublicKey());
        Ledger destination = ledgerService.findLedgerByPublicKey(operationDTO.getDestinationPublicKey());

        if(origin == null) {
            throw new LedgerDoesNotExistException(operationDTO.getOriginPublicKey());
        }
        if(destination == null) {
            throw new LedgerDoesNotExistException(operationDTO.getDestinationPublicKey());
        }
        Long ledgerDebt = 0L;
        for(Operation op : origin.getOperations()) {
            if(!op.getCommitted()) {
                if(op.getType().equals(OperationType.OUTCOMING))
                    ledgerDebt += op.getValue();
                if(op.getType().equals(OperationType.INCOMING))
                    ledgerDebt -= op.getValue();
            }
        }
        if(origin.getBalance() <= operationDTO.getValue() || origin.getBalance() <= ledgerDebt) {
            throw new LedgerHasNoFundsException(origin.getPublicKey());
        }

        Operation operationDest = new Operation(origin, OperationType.INCOMING, operationDTO.getValue());
        Operation operationOri = new Operation(destination, OperationType.OUTCOMING, operationDTO.getValue());

		// save signature with the operation
		operationOri.setSignature(Base64.getEncoder().encodeToString(signature));

		operationOri = this.saveOperation(operationOri);
        operationDest.setOriginOperationID(operationOri.getId());
		operationDest = this.saveOperation(operationDest);

        origin.addOperation(operationOri);
        destination.addOperation(operationDest);

        this.ledgerService.saveLedger(origin);
        this.ledgerService.saveLedger(destination);
    }

    public void receiveOperation(ReceiveOperationDTO receiveOperationDTO, byte[] signature) throws OperationDoesNotExistException {
        Ledger receivingLedger = ledgerService.findLedgerByPublicKey(receiveOperationDTO.getPublicKey());
        List<Operation> committedOperations = receivingLedger.getOperations().stream().filter(operation -> operation.getId().equals(receiveOperationDTO.getOperationId())).collect(Collectors.toList());

		if (committedOperations == null || committedOperations.isEmpty()) {
            throw new OperationDoesNotExistException(receiveOperationDTO.getOperationId());
        }

        for (Operation op : receivingLedger.getOperations()) {
            if (op.getId().equals(receiveOperationDTO.getOperationId())) {
				// update receiving ledger balance
				receivingLedger.setBalance(receivingLedger.getBalance() + op.getValue());

				// set sender ledger new balance
				Ledger senderLeder = ledgerService.findLedgerByPublicKey(op.getLedger().getPublicKey());
				senderLeder.setBalance(senderLeder.getBalance() - op.getValue());
				this.ledgerService.saveLedger(senderLeder);

				// set origin operation to committed
				Operation originOperation = operationRepository.findOperationById(op.getOriginOperationID());
				originOperation.setCommitted(true);
				this.saveOperation(originOperation);

				// set destination operation to committed and add message's signature
				op.setCommitted(true);
				op.setSignature(Base64.getEncoder().encodeToString(signature));
				this.saveOperation(op);

                break;
            }
        }

		// persist receiving ledger changes
        this.ledgerService.saveLedger(receivingLedger);
    }
    
	public Operation saveOperation(Operation op) {
		return operationRepository.save(op);
    }
}
