package com.sec.cryptohds.service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.domain.OperationType;
import com.sec.cryptohds.repository.OperationRepository;
import com.sec.cryptohds.service.dto.OperationDTO;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;
import com.sec.cryptohds.service.exceptions.LedgerHasNoFundsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;

    private final LedgerService ledgerService;
    
    public LedgerService getLedgerService() {
    	return ledgerService;
    }

    public OperationService(OperationRepository operationRepository, LedgerService ledgerService) {
        this.operationRepository = operationRepository;
        this.ledgerService = ledgerService;
    }

    public void createOperation(OperationDTO operationDTO) throws CryptohdsException {
        Ledger origin = ledgerService.findLedgerByPublicKey(operationDTO.getOrigin().getPublicKey());
        Ledger destination = ledgerService.findLedgerByPublicKey(operationDTO.getDestination().getPublicKey());

        if(origin == null) {
            throw new LedgerDoesNotExistException(operationDTO.getOrigin().getPublicKey());
        }
        if(destination == null) {
            throw new LedgerDoesNotExistException(operationDTO.getDestination().getPublicKey());
        }
        Long ledgerDebt = 0L;
        for(Operation op : origin.getOperations()) {
            if(!op.getCommitted()) {
                ledgerDebt += op.getValue();
            }
        }
        if(origin.getBalance() <= operationDTO.getValue() || origin.getBalance() <= ledgerDebt) {
            throw new LedgerHasNoFundsException(origin.getPublicKey());
        }

        Operation operationDest = new Operation(origin, OperationType.INCOMING, operationDTO.getValue());
        Operation operationOri = new Operation(destination, OperationType.OUTCOMING, operationDTO.getValue());

        operationDest = operationRepository.save(operationDest);
        operationOri = operationRepository.save(operationOri);

        origin.addOperation(operationOri);
        destination.addOperation(operationDest);

        this.ledgerService.saveLedger(origin);
        this.ledgerService.saveLedger(destination);
    }
}
