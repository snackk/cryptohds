package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.dto.LedgerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LedgerResource {

    private final Logger log = LoggerFactory.getLogger(LedgerResource.class);

    private final LedgerService ledgerService;

    public LedgerResource(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * POST  /ledgers  : Creates a new ledger.
     * <p>
     * Creates a new ledger if not already used.
     *
     * @param ledgerDTO the ledger to create
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request) if
     * @throws LedgerAlreadyExistsException is throw.
     */
    @PostMapping("/ledgers")
    public ResponseEntity<?> createLedger(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerAlreadyExistsException {
        log.debug("REST request to create Ledger : {}", ledgerDTO);

        if (ledgerService.existsLedger(ledgerDTO)) {
            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
        } else {
            ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    
    /**
     * GET  /balance  : Returns balance of ledger.
     * <p>
     * Returns balance of ledger if it exists.
     *
     * @param ledgerDTO the ledger needed to extract the balance
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request) if
     * @throws LedgerDoesNotExistException is throw.
     */
    @PostMapping("/balance") //TODO 
    public ResponseEntity<?> checkBalance(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerDoesNotExistException {
        log.debug("REST request to check Ledger's balance : {}", ledgerDTO);

        if (!(ledgerService.existsLedger(ledgerDTO))) {
            throw new LedgerDoesNotExistException();
        } else {
            ledgerService.getbalancefromLedger(ledgerDTO.getPublicKey());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //TODO retornar o saldo numa msg http ??
        }
    }
    
    /**
     * GET  /audit  : Return all operations of a ledger.
     * <p>
     * Retorna List <OperationDTO> with all operations.
     *
     * @param ledgerDTO the ledger needed to extract the balance
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request) if
     * @throws LedgerDoesNotExistException is throw.
     */
    @PostMapping("/audit")//TODO  
    public ResponseEntity<?> audit(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerDoesNotExistException {
        log.debug("REST request to check Ledger's operations : {}", ledgerDTO);

        if (!(ledgerService.existsLedger(ledgerDTO))) {
            throw new LedgerDoesNotExistException();
        } else {
            ledgerService.getOperationsFromLedger(ledgerDTO.getPublicKey());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //TODO retornar as msgs das operacoes http ??
        }
    }
    
    
}
  
    
    
    
    
  
