package com.sec.cryptohds.service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.repository.OperationRepository;
import com.sec.cryptohds.service.dto.OperationDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;

    private final LedgerService ledgerService;

    public OperationService(OperationRepository operationRepository, LedgerService ledgerService) {
        this.operationRepository = operationRepository;
        this.ledgerService = ledgerService;
    }

    public Operation createOperation(OperationDTO operationDTO) {
        Ledger origin = ledgerService.findLedgerByPublicKey(operationDTO.getOrigin().getPublicKey());
        Operation operation = new Operation(origin, operationDTO.getType(), operationDTO.getValue());

        operation = operationRepository.save(operation);

        origin.addOperation(operation);
        this.ledgerService.saveLedger(origin);

        return operation;
    }
}
