package com.sec.cryptohds.service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.repository.LedgerRepository;
import com.sec.cryptohds.service.dto.LedgerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public Ledger registerLedger(LedgerDTO ledgerDTO) {
        Ledger ledger = new Ledger();
        ledger.setPublicKey(ledgerDTO.getPublicKey());

        return ledgerRepository.save(ledger);
    }

    public boolean existsLedger(LedgerDTO ledgerDTO) {
        if(this.ledgerRepository.findById(ledgerDTO.getId()) == null)
            return false;
        else return true;
    }

    public List<Ledger> findLedgers() {
        return ledgerRepository.findAll();
    }

    public boolean existsAnyLedger() {
        return ledgerRepository.findAll().size() > 1 ? true : false;
    }
}
