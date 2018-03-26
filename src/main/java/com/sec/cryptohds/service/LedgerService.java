package com.sec.cryptohds.service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.repository.LedgerRepository;
import com.sec.cryptohds.service.dto.LedgerBalanceDTO;
import com.sec.cryptohds.service.dto.LedgerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public Ledger registerLedger(LedgerDTO ledgerDTO) {
        Ledger ledger = new Ledger(ledgerDTO.getName(), ledgerDTO.getPublicKey());
        return ledgerRepository.save(ledger);
    }
    
    public LedgerBalanceDTO getBalanceFromLedger(String publicKey) {
        List<Operation> operations = getOperationsFromLedger(publicKey);
        List<Operation> uncommittedOperations = operations.stream().filter(operation -> !operation.getCommitted()).collect(Collectors.toList());

        return new LedgerBalanceDTO(findLedgerByPublicKey(publicKey).getBalance(), uncommittedOperations);
    }
    
    public List<Operation> getOperationsFromLedger(String pubKey) {
    	return findLedgerByPublicKey(pubKey).getOperations();
    
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
