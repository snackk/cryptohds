package com.sec.cryptohds.service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.repository.LedgerRepository;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
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
        List<Operation> operations = getOperationsFromLedger(publicKey);
        List<Operation> uncommittedOperations = operations.stream().filter(operation -> !operation.getCommitted()).collect(Collectors.toList());

        List<OperationDTO> uncommittedOperationsDTO = new ArrayList<>();
        for(Operation uop : uncommittedOperations) {
            uncommittedOperationsDTO.add(new OperationDTO(uop.getId(), uop.getTimestamp(), uop.getValue(), uop.getCommitted(), uop.getType().toString(),
                    uop.getLedger().getPublicKey(), publicKey));
        }

        return new LedgerBalanceDTO(findLedgerByPublicKey(publicKey).getBalance(), uncommittedOperationsDTO);
    }
    
    public OperationListDTO getOperationsDTOFromLedger(String publicKey) {

        List<OperationDTO> operationsDTO = new ArrayList<>();
        for(Operation uop : getOperationsFromLedger(publicKey)) {
            operationsDTO.add(new OperationDTO(uop.getId(), uop.getTimestamp(), uop.getValue(), uop.getCommitted(), uop.getType().toString(),
                    uop.getLedger().getPublicKey(), publicKey));
        }
    	OperationListDTO op = new OperationListDTO(operationsDTO);
        return op;
    }
    

    private List<Operation> getOperationsFromLedger(String publicKey) {
        return findLedgerByPublicKey(publicKey).getOperations();
    }

    public boolean existsLedger(String publicKey) {
        if(this.ledgerRepository.findLedgerByPublicKey(publicKey) == null)
            return false;
        else return true;
    }

    public Ledger findLedgerByPublicKey(String publicKey) {
        return this.ledgerRepository.findLedgerByPublicKey(publicKey);
    }

    public List<Ledger> findLedgers() {
        return this.ledgerRepository.findAll();
    }

    public boolean existsAnyLedger() {
        return this.ledgerRepository.findAll().size() > 1 ? true : false;
    }

    public void saveLedger(Ledger ledger) {
        this.ledgerRepository.save(ledger);
    }
}
