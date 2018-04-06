package com.sec.cryptohds.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;

    private final LedgerService ledgerService;

    public OperationService(OperationRepository operationRepository, LedgerService ledgerService) {
        this.operationRepository = operationRepository;
        this.ledgerService = ledgerService;
    }

    public void createOperation(OperationDTO operationDTO) throws CryptohdsException {
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

        operationOri = operationRepository.save(operationOri);
        operationDest.setOriginOperationID(operationOri.getId());
        operationDest = operationRepository.save(operationDest);

        origin.addOperation(operationOri);
        destination.addOperation(operationDest);

        this.ledgerService.saveLedger(origin);
        this.ledgerService.saveLedger(destination);
    }

    public void receiveOperation(ReceiveOperationDTO receiveOperationDTO) throws OperationDoesNotExistException {
        Ledger ledger = ledgerService.findLedgerByPublicKey(receiveOperationDTO.getPublicKey());
        List<Operation> committedOperations = ledger.getOperations().stream().filter(operation -> operation.getId().equals(receiveOperationDTO.getOperationId())).collect(Collectors.toList());

        if (committedOperations == null || (committedOperations != null && committedOperations.size() < 1)) {
            throw new OperationDoesNotExistException(receiveOperationDTO.getOperationId());
        }

        for (Operation op : ledger.getOperations()) {
            if (op.getId().equals(receiveOperationDTO.getOperationId())) {
                op.setCommitted(true);

                Operation originOperation = operationRepository.findOperationById(op.getOriginOperationID());
                originOperation.setCommitted(true);

                ledger.setBalance(ledger.getBalance() + op.getValue());

                Ledger origin = ledgerService.findLedgerByPublicKey(op.getLedger().getPublicKey());
                origin.setBalance(origin.getBalance() - op.getValue());
                this.ledgerService.saveLedger(origin);

                break;
            }
        }
        this.ledgerService.saveLedger(ledger);
    }
}
