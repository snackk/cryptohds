package com.sec.cryptohds.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.repository.LedgerRepository;
import com.sec.cryptohds.repository.OperationRepository;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;

	private final OperationRepository operationRepository;

    public LedgerService(LedgerRepository ledgerRepository, OperationRepository operationRepository) {
        this.ledgerRepository = ledgerRepository;
        this.operationRepository = operationRepository;
    }

    public void updateLedgerSeqNumber(LedgerDTO ledgerDTO) {
        Ledger ledger = findLedgerByPublicKey(ledgerDTO.getPublicKey());
        ledgerDTO.setSeqNumber(ledger.getSeqNumber());
    }

    public Ledger registerLedger(LedgerDTO ledgerDTO) {
        Ledger ledger = new Ledger(ledgerDTO.getName(), ledgerDTO.getPublicKey());
        return ledgerRepository.save(ledger);
    }

    public LedgerBalanceDTO getBalanceFromLedger(String publicKey) {
		// find ledger
		Ledger ledger = findLedgerByPublicKey(publicKey);

        if(ledger == null) {
            throw new LedgerDoesNotExistException(publicKey);
        }
        
		// get all operations
		List<Operation> operations = operationRepository.findAll();

		// filter by uncommitted operations
        List<Operation> uncommittedOperations = operations.stream().filter(operation -> !operation.isCommitted()).collect(Collectors.toList());

        List<OperationDTO> uncommittedOperationsDTO = new ArrayList<>();

		// get all uncommitted operations for ledger
        for(Operation op : uncommittedOperations) {
        	if (op.getOriginLedger().equals(ledger) || op.getDestinationLedger().equals(ledger)) {
        		uncommittedOperationsDTO.add(new OperationDTO(op.getId(), op.getTimestamp(), op.getValue(), op.isCommitted(), op.getOriginLedger().getPublicKey(), publicKey));
        	}
        }

        return new LedgerBalanceDTO(ledger.getBalance(), uncommittedOperationsDTO);
    }

    public OperationListDTO getOperationsDTOFromLedger(String publicKey) {
		// find ledger
		Ledger ledger = findLedgerByPublicKey(publicKey);

        if(ledger == null) {
            throw new LedgerDoesNotExistException(publicKey);
        }

        List<OperationDTO> operationsDTO = new ArrayList<>();

		// get all operations for ledger
		for (Operation op : operationRepository.findAll()) {
			if (op.getOriginLedger().equals(ledger) || op.getDestinationLedger().equals(ledger)) {
				operationsDTO.add(new OperationDTO(op.getId(), op.getTimestamp(), op.getValue(), op.isCommitted(), op.getOriginLedger().getPublicKey(), publicKey));
			}
        }

		OperationListDTO opList = new OperationListDTO(operationsDTO);

		return opList;
    }

    public boolean existsLedger(String publicKey) {
        if(this.ledgerRepository.findLedgerByPublicKey(publicKey) == null)
            return false;
        else return true;
    }

    public Ledger findLedgerByPublicKey(String publicKey) {
        return this.ledgerRepository.findLedgerByPublicKey(publicKey);
    }

//    public List<Ledger> findLedgers() {
//        return this.ledgerRepository.findAll();
//    }

//    public boolean existsAnyLedger() {
//        return this.ledgerRepository.findAll().size() > 1 ? true : false;
//    }

    public void saveLedger(Ledger ledger) {
        this.ledgerRepository.save(ledger);
    }
}
